package org.ubp.edu.ar.ejemplocompletofx.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.ubp.edu.ar.ejemplocompletofx.dto.DistribuidorDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.ZonaDto;

public class DistribuidorDao implements Dao<DistribuidorDto> {

    private static final String SELECT_DISTRIBUIDOR
            = "SELECT d.id, d.legajo, d.nombre, d.apellido, d.capacidad_diaria, "
            + "z.id zona_id, z.nombre zona_nombre "
            + "FROM distribuidor d JOIN zona z ON z.id = d.zona_id ";

    @Override
    public DistribuidorDto buscar(DistribuidorDto dto) {
        String sql = SELECT_DISTRIBUIDOR + "WHERE d.id = ? OR d.legajo = ?";
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            statement.setInt(1, dto.getId());
            statement.setString(2, dto.getLegajo());
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo buscar el distribuidor", ex);
        }
    }

    @Override
    public List<DistribuidorDto> listarPorCriterio(DistribuidorDto dto) {
        String sql = SELECT_DISTRIBUIDOR + "WHERE z.id = ? ORDER BY d.apellido, d.nombre";
        List<DistribuidorDto> distribuidores = new ArrayList<>();
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql)) {
            statement.setInt(1, dto.getZona().getId());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    distribuidores.add(mapear(rs));
                }
            }
            return distribuidores;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudieron listar los distribuidores", ex);
        }
    }

    @Override
    public List<DistribuidorDto> listarTodos() {
        String sql = SELECT_DISTRIBUIDOR + "ORDER BY z.nombre";
        List<DistribuidorDto> distribuidores = new ArrayList<>();
        try (ConexionSql conexion = new ConexionSql();
                PreparedStatement statement = conexion.getConnection().prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                distribuidores.add(mapear(rs));
            }
            return distribuidores;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudieron listar los distribuidores", ex);
        }
    }

    @Override
    public boolean insertar(DistribuidorDto dto) {
        throw new UnsupportedOperationException("Alta de distribuidores fuera de alcance");
    }

    @Override
    public boolean modificar(DistribuidorDto dto) {
        throw new UnsupportedOperationException("Modificacion de distribuidores fuera de alcance");
    }

    @Override
    public boolean borrar(DistribuidorDto dto) {
        throw new UnsupportedOperationException("Baja de distribuidores fuera de alcance");
    }

    private DistribuidorDto mapear(ResultSet rs) throws SQLException {
        return new DistribuidorDto(
                rs.getInt("id"),
                rs.getString("legajo"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getInt("capacidad_diaria"),
                new ZonaDto(rs.getInt("zona_id"), rs.getString("zona_nombre")));
    }
}
