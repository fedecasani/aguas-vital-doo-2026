package org.ubp.edu.ar.ejemplocompletofx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.ubp.edu.ar.ejemplocompletofx.dto.BarrioDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.ClienteDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.ZonaDto;

public class ClienteDao implements Dao<ClienteDto> {

    private static final String SELECT_CLIENTE
            = "SELECT c.id, c.tipo_documento, c.nro_documento, c.nombre, c.apellido, "
            + "c.razon_social, c.direccion, c.telefono, c.activo, "
            + "b.id barrio_id, b.nombre barrio_nombre, z.id zona_id, z.nombre zona_nombre "
            + "FROM cliente c "
            + "JOIN barrio b ON b.id = c.barrio_id "
            + "JOIN zona z ON z.id = b.zona_id ";

    @Override
    public ClienteDto buscar(ClienteDto dto) {
        String sql = SELECT_CLIENTE + "WHERE c.tipo_documento = ? AND c.nro_documento = ?";
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            statement.setString(1, dto.getTipoDocumento());
            statement.setString(2, dto.getDni());
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo buscar el cliente", ex);
        }
    }

    @Override
    public List<ClienteDto> listarPorCriterio(ClienteDto dto) {
        String sql = SELECT_CLIENTE
                + "WHERE c.activo = 1 AND (c.nro_documento LIKE ? OR c.apellido LIKE ?) "
                + "ORDER BY c.apellido, c.nombre";
        List<ClienteDto> clientes = new ArrayList<>();
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            String criterio = "%" + (dto.getDni() == null ? "" : dto.getDni()) + "%";
            statement.setString(1, criterio);
            statement.setString(2, criterio);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapear(rs));
                }
            }
            return clientes;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudieron listar los clientes", ex);
        }
    }

    @Override
    public List<ClienteDto> listarTodos() {
        String sql = SELECT_CLIENTE + "WHERE c.activo = 1 ORDER BY c.apellido, c.nombre";
        List<ClienteDto> clientes = new ArrayList<>();
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                clientes.add(mapear(rs));
            }
            return clientes;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudieron listar los clientes", ex);
        }
    }

    @Override
    public boolean insertar(ClienteDto dto) {
        String sql = "INSERT INTO cliente (tipo_documento, nro_documento, nombre, apellido, "
                + "razon_social, direccion, telefono, barrio_id, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)";
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            statement.setString(1, dto.getTipoDocumento());
            statement.setString(2, dto.getDni());
            statement.setString(3, dto.getNombre());
            statement.setString(4, dto.getApellido());
            statement.setString(5, dto.getRazonSocial());
            statement.setString(6, dto.getDireccion());
            statement.setString(7, dto.getTelefono());
            statement.setInt(8, dto.getBarrio().getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo registrar el cliente", ex);
        }
    }

    @Override
    public boolean modificar(ClienteDto dto) {
        String sql = "UPDATE cliente SET tipo_documento = ?, nro_documento = ?, nombre = ?, apellido = ?, "
                + "razon_social = ?, direccion = ?, telefono = ?, barrio_id = ? WHERE id = ?";
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            statement.setString(1, dto.getTipoDocumento());
            statement.setString(2, dto.getDni());
            statement.setString(3, dto.getNombre());
            statement.setString(4, dto.getApellido());
            statement.setString(5, dto.getRazonSocial());
            statement.setString(6, dto.getDireccion());
            statement.setString(7, dto.getTelefono());
            statement.setInt(8, dto.getBarrio().getId());
            statement.setInt(9, dto.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo actualizar el cliente", ex);
        }
    }

    @Override
    public boolean borrar(ClienteDto dto) {
        String sql = "UPDATE cliente SET activo = 0 WHERE id = ?";
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            statement.setInt(1, dto.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo eliminar el cliente", ex);
        }
    }

    private ClienteDto mapear(ResultSet rs) throws SQLException {
        ClienteDto cliente = new ClienteDto();
        cliente.setId(rs.getInt("id"));
        cliente.setTipoDocumento(rs.getString("tipo_documento"));
        cliente.setDni(rs.getString("nro_documento"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setApellido(rs.getString("apellido"));
        cliente.setRazonSocial(rs.getString("razon_social"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setActivo(rs.getBoolean("activo"));
        ZonaDto zona = new ZonaDto(rs.getInt("zona_id"), rs.getString("zona_nombre"));
        cliente.setBarrio(new BarrioDto(
                rs.getInt("barrio_id"), rs.getString("barrio_nombre"), zona));
        return cliente;
    }
}
