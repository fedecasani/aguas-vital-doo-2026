package org.ubp.edu.ar.ejemplocompletofx.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.ubp.edu.ar.ejemplocompletofx.dto.BarrioDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.ZonaDto;

public class BarrioDao {

    private static final String SELECT_BARRIOS
            = "SELECT b.id, b.nombre, z.id zona_id, z.nombre zona_nombre "
            + "FROM barrio b "
            + "JOIN zona z ON z.id = b.zona_id "
            + "ORDER BY z.nombre, b.nombre";

    public List<BarrioDto> listarTodos() {
        List<BarrioDto> barrios = new ArrayList<>();
        try (ConexionSql conexion = new ConexionSql();
                Statement statement = conexion.getConnection().createStatement();
                ResultSet rs = statement.executeQuery(SELECT_BARRIOS)) {
            while (rs.next()) {
                barrios.add(mapear(rs));
            }
            return barrios;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudieron listar los barrios", ex);
        }
    }

    private BarrioDto mapear(ResultSet rs) throws SQLException {
        ZonaDto zona = new ZonaDto(rs.getInt("zona_id"), rs.getString("zona_nombre"));
        return new BarrioDto(rs.getInt("id"), rs.getString("nombre"), zona);
    }
}
