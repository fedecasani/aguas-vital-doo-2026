package org.ubp.edu.ar.ejemplocompletofx.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.ubp.edu.ar.ejemplocompletofx.dto.ProductoDto;

public class ProductoDao implements Dao<ProductoDto> {

    private static final String SELECT_PRODUCTO
            = "SELECT id, codigo, nombre, tipo, capacidad_litros, precio, activo FROM producto ";

    @Override
    public ProductoDto buscar(ProductoDto dto) {
        String sql = SELECT_PRODUCTO + "WHERE codigo = ?";
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            statement.setString(1, dto.getCodBarra());
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo buscar el producto", ex);
        }
    }

    @Override
    public List<ProductoDto> listarPorCriterio(ProductoDto dto) {
        String sql = SELECT_PRODUCTO
                + "WHERE activo = 1 AND (codigo LIKE ? OR nombre LIKE ?) ORDER BY nombre";
        List<ProductoDto> productos = new ArrayList<>();
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            String criterio = "%" + (dto.getNombre() == null ? "" : dto.getNombre()) + "%";
            statement.setString(1, criterio);
            statement.setString(2, criterio);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapear(rs));
                }
            }
            return productos;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudieron listar los productos", ex);
        }
    }

    @Override
    public List<ProductoDto> listarTodos() {
        String sql = SELECT_PRODUCTO + "WHERE activo = 1 ORDER BY tipo, capacidad_litros";
        List<ProductoDto> productos = new ArrayList<>();
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                productos.add(mapear(rs));
            }
            return productos;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudieron listar los productos", ex);
        }
    }

    @Override
    public boolean insertar(ProductoDto dto) {
        String sql = "INSERT INTO producto (codigo, nombre, tipo, capacidad_litros, precio, activo) "
                + "VALUES (?, ?, ?, ?, ?, 1)";
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            statement.setString(1, dto.getCodBarra());
            statement.setString(2, dto.getNombre());
            statement.setString(3, dto.getTipo());
            statement.setInt(4, dto.getCapacidadLitros());
            statement.setFloat(5, dto.getPrecio());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo registrar el producto", ex);
        }
    }

    @Override
    public boolean modificar(ProductoDto dto) {
        String sql = "UPDATE producto SET codigo = ?, nombre = ?, tipo = ?, "
                + "capacidad_litros = ?, precio = ? WHERE id = ?";
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            statement.setString(1, dto.getCodBarra());
            statement.setString(2, dto.getNombre());
            statement.setString(3, dto.getTipo());
            statement.setInt(4, dto.getCapacidadLitros());
            statement.setFloat(5, dto.getPrecio());
            statement.setInt(6, dto.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo actualizar el producto", ex);
        }
    }

    @Override
    public boolean borrar(ProductoDto dto) {
        String sql = "UPDATE producto SET activo = 0 WHERE id = ?";
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            statement.setInt(1, dto.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo eliminar el producto", ex);
        }
    }

    private ProductoDto mapear(ResultSet rs) throws SQLException {
        ProductoDto producto = new ProductoDto();
        producto.setId(rs.getInt("id"));
        producto.setCodBarra(rs.getString("codigo"));
        producto.setNombre(rs.getString("nombre"));
        producto.setTipo(rs.getString("tipo"));
        producto.setCapacidadLitros(rs.getInt("capacidad_litros"));
        producto.setPrecio(rs.getFloat("precio"));
        producto.setCantidad(0);
        producto.setActivo(rs.getBoolean("activo"));
        return producto;
    }
}
