package org.ubp.edu.ar.ejemplocompletofx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.ubp.edu.ar.ejemplocompletofx.dto.BarrioDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.ClienteDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.DetallePedidoDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.DistribuidorDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.PagoDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.PedidoDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.ProductoDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.ZonaDto;

public class PedidoDao implements Dao<PedidoDto> {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String SELECT_PEDIDO
            = "SELECT p.id, p.nro, p.fecha_solicitud, p.fecha_estimada, p.fecha_entrega, "
            + "p.estado, p.observacion, "
            + "c.id cliente_id, c.tipo_documento, c.nro_documento, c.nombre cliente_nombre, "
            + "c.apellido cliente_apellido, c.razon_social, c.direccion, c.telefono, c.activo, "
            + "b.id barrio_id, b.nombre barrio_nombre, z.id zona_id, z.nombre zona_nombre, "
            + "d.id distribuidor_id, d.legajo, d.nombre distribuidor_nombre, "
            + "d.apellido distribuidor_apellido, d.capacidad_diaria "
            + "FROM pedido p "
            + "JOIN cliente c ON c.id = p.cliente_id "
            + "JOIN barrio b ON b.id = c.barrio_id "
            + "JOIN zona z ON z.id = b.zona_id "
            + "JOIN distribuidor d ON d.id = p.distribuidor_id ";

    @Override
    public PedidoDto buscar(PedidoDto dto) {
        String sql = SELECT_PEDIDO + "WHERE p.nro = ?";
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            statement.setInt(1, dto.getNro());
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                PedidoDto pedido = mapearPedido(rs);
                pedido.setDetalles(listarDetalles(conexion.getConnection(), pedido));
                return pedido;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo buscar el pedido", ex);
        }
    }

    @Override
    public List<PedidoDto> listarPorCriterio(PedidoDto dto) {
        String sql = SELECT_PEDIDO + "WHERE p.nro = ? ORDER BY p.nro";
        return ejecutarListado(sql, dto.getNro());
    }

    @Override
    public List<PedidoDto> listarTodos() {
        String sql = SELECT_PEDIDO + "ORDER BY p.nro DESC";
        return ejecutarListado(sql, null);
    }

    @Override
    public boolean insertar(PedidoDto dto) {
        String insertPedido = "INSERT INTO pedido (nro, fecha_solicitud, fecha_estimada, "
                + "cliente_id, distribuidor_id, estado) "
                + "VALUES ((SELECT COALESCE(MAX(nro), 0) + 1 FROM pedido), ?, ?, ?, ?, 'PENDIENTE')";
        String insertDetalle = "INSERT INTO detallepedido "
                + "(pedido_id, producto_id, cantidad, precio_venta) VALUES (?, ?, ?, ?)";

        try (ConexionSql conexion = new ConexionSql()) {
            Connection connection = conexion.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement pedidoStatement = connection.prepareStatement(
                    insertPedido, Statement.RETURN_GENERATED_KEYS)) {
                pedidoStatement.setString(1, format(dto.getFecha()));
                pedidoStatement.setString(2, format(dto.getFechaEstimada()));
                pedidoStatement.setInt(3, dto.getCliente().getId());
                pedidoStatement.setInt(4, dto.getDistribuidor().getId());
                if (pedidoStatement.executeUpdate() != 1) {
                    connection.rollback();
                    return false;
                }
                int pedidoId;
                try (ResultSet keys = pedidoStatement.getGeneratedKeys()) {
                    if (!keys.next()) {
                        connection.rollback();
                        return false;
                    }
                    pedidoId = keys.getInt(1);
                }
                try (PreparedStatement detalleStatement = connection.prepareStatement(insertDetalle)) {
                    for (DetallePedidoDto detalle : dto.getDetalles()) {
                        detalleStatement.setInt(1, pedidoId);
                        detalleStatement.setInt(2, detalle.getProducto().getId());
                        detalleStatement.setFloat(3, detalle.getCantidad());
                        detalleStatement.setFloat(4, detalle.getPrecioVta());
                        detalleStatement.addBatch();
                    }
                    detalleStatement.executeBatch();
                }
                connection.commit();
                return true;
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo registrar el pedido", ex);
        }
    }

    @Override
    public boolean modificar(PedidoDto dto) {
        String sql = "UPDATE pedido SET estado = ?, fecha_entrega = ?, observacion = ? WHERE nro = ?";
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            statement.setString(1, dto.getEstado());
            statement.setString(2, dto.getFechaEntrega() == null ? null : format(dto.getFechaEntrega()));
            statement.setString(3, dto.getObservacion());
            statement.setInt(4, dto.getNro());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo actualizar el pedido", ex);
        }
    }

    public boolean actualizarConPago(PedidoDto pedido, PagoDto pago) {
        String updatePedido = "UPDATE pedido SET estado = 'ENTREGADO', fecha_entrega = ?, "
                + "observacion = ? WHERE nro = ? AND estado = 'PENDIENTE'";
        String insertPago = "INSERT INTO pago (pedido_id, fecha_hora, forma_pago, monto) "
                + "SELECT id, ?, ?, ? FROM pedido WHERE nro = ?";
        try (ConexionSql conexion = new ConexionSql()) {
            Connection connection = conexion.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement pedidoStatement = connection.prepareStatement(updatePedido);
                    PreparedStatement pagoStatement = connection.prepareStatement(insertPago)) {
                pedidoStatement.setString(1, format(pedido.getFechaEntrega()));
                pedidoStatement.setString(2, pedido.getObservacion());
                pedidoStatement.setInt(3, pedido.getNro());
                if (pedidoStatement.executeUpdate() != 1) {
                    connection.rollback();
                    return false;
                }
                pagoStatement.setString(1, format(pago.getFechaHora()));
                pagoStatement.setString(2, pago.getFormaPago().name());
                pagoStatement.setFloat(3, pago.getMonto());
                pagoStatement.setInt(4, pedido.getNro());
                if (pagoStatement.executeUpdate() != 1) {
                    connection.rollback();
                    return false;
                }
                connection.commit();
                return true;
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo registrar la entrega y el pago", ex);
        }
    }

    @Override
    public boolean borrar(PedidoDto dto) {
        String sql = "UPDATE pedido SET estado = 'CANCELADO', observacion = ? "
                + "WHERE nro = ? AND estado = 'PENDIENTE'";
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            statement.setString(1, dto.getObservacion());
            statement.setInt(2, dto.getNro());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo cancelar el pedido", ex);
        }
    }

    public DistribuidorDto buscarDistribuidorPorZona(int zonaId) {
        DistribuidorDto criterio = new DistribuidorDto();
        criterio.setZona(new ZonaDto(zonaId, null));
        List<DistribuidorDto> distribuidores = new DistribuidorDao().listarPorCriterio(criterio);
        return distribuidores.isEmpty() ? null : distribuidores.get(0);
    }

    public Date calcularFechaEstimada(DistribuidorDto distribuidor, Date fechaSolicitud) {
        String sql = "SELECT COUNT(*) FROM pedido "
                + "WHERE distribuidor_id = ? AND estado = 'PENDIENTE'";
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            statement.setInt(1, distribuidor.getId());
            int pendientes;
            try (ResultSet rs = statement.executeQuery()) {
                pendientes = rs.next() ? rs.getInt(1) : 0;
            }
            int diasAdicionales = pendientes / distribuidor.getCapacidadDiaria();
            int turno = pendientes % distribuidor.getCapacidadDiaria();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaSolicitud);
            calendar.add(Calendar.DAY_OF_MONTH, diasAdicionales + 1);
            calendar.set(Calendar.HOUR_OF_DAY, 9 + turno);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo calcular la fecha estimada", ex);
        }
    }

    private List<PedidoDto> ejecutarListado(String sql, Integer nro) {
        List<PedidoDto> pedidos = new ArrayList<>();
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            if (nro != null) {
                statement.setInt(1, nro);
            }
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }
            return pedidos;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudieron listar los pedidos", ex);
        }
    }

    private List<DetallePedidoDto> listarDetalles(Connection connection, PedidoDto pedido)
            throws SQLException {
        String sql = "SELECT dp.id, dp.cantidad, dp.precio_venta, "
                + "pr.id producto_id, pr.codigo, pr.nombre, pr.tipo, "
                + "pr.capacidad_litros, pr.precio, pr.activo "
                + "FROM detallepedido dp JOIN producto pr ON pr.id = dp.producto_id "
                + "WHERE dp.pedido_id = ?";
        List<DetallePedidoDto> detalles = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pedido.getId());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ProductoDto producto = new ProductoDto();
                    producto.setId(rs.getInt("producto_id"));
                    producto.setCodBarra(rs.getString("codigo"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setTipo(rs.getString("tipo"));
                    producto.setCapacidadLitros(rs.getInt("capacidad_litros"));
                    producto.setPrecio(rs.getFloat("precio"));
                    producto.setActivo(rs.getBoolean("activo"));
                    detalles.add(new DetallePedidoDto(
                            rs.getInt("id"), pedido, producto,
                            rs.getFloat("cantidad"), rs.getFloat("precio_venta")));
                }
            }
        }
        return detalles;
    }

    private PedidoDto mapearPedido(ResultSet rs) throws SQLException {
        ZonaDto zona = new ZonaDto(rs.getInt("zona_id"), rs.getString("zona_nombre"));
        BarrioDto barrio = new BarrioDto(
                rs.getInt("barrio_id"), rs.getString("barrio_nombre"), zona);
        ClienteDto cliente = new ClienteDto();
        cliente.setId(rs.getInt("cliente_id"));
        cliente.setTipoDocumento(rs.getString("tipo_documento"));
        cliente.setDni(rs.getString("nro_documento"));
        cliente.setNombre(rs.getString("cliente_nombre"));
        cliente.setApellido(rs.getString("cliente_apellido"));
        cliente.setRazonSocial(rs.getString("razon_social"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setActivo(rs.getBoolean("activo"));
        cliente.setBarrio(barrio);
        DistribuidorDto distribuidor = new DistribuidorDto(
                rs.getInt("distribuidor_id"), rs.getString("legajo"),
                rs.getString("distribuidor_nombre"), rs.getString("distribuidor_apellido"),
                rs.getInt("capacidad_diaria"), zona);
        PedidoDto pedido = new PedidoDto();
        pedido.setId(rs.getInt("id"));
        pedido.setNro(rs.getInt("nro"));
        pedido.setFecha(parse(rs.getString("fecha_solicitud")));
        pedido.setFechaEstimada(parse(rs.getString("fecha_estimada")));
        pedido.setFechaEntrega(parse(rs.getString("fecha_entrega")));
        pedido.setEstado(rs.getString("estado"));
        pedido.setObservacion(rs.getString("observacion"));
        pedido.setCliente(cliente);
        pedido.setDistribuidor(distribuidor);
        pedido.setDetalles(new ArrayList<>());
        return pedido;
    }

    private String format(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(DATE_PATTERN).format(date);
    }

    private Date parse(String value) {
        if (value == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(DATE_PATTERN).parse(value);
        } catch (ParseException ex) {
            throw new IllegalStateException("Fecha invalida en la base de datos: " + value, ex);
        }
    }
}
