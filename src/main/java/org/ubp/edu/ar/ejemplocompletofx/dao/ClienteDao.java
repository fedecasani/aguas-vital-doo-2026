/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.ubp.edu.ar.ejemplocompletofx.dto.ClienteDto;

/**
 *
 * @author agustin
 */
public class ClienteDao implements Dao<ClienteDto> {

    private ConexionSql conexion;

    @Override
    public ClienteDto buscar(ClienteDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ClienteDto> listarPorCriterio(ClienteDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ClienteDto> listarTodos() {
        this.conexion = new ConexionSql();
        Connection con;
        Statement sentencia = null;
        ResultSet rs = null;
        List<ClienteDto> lista = new ArrayList<>();

        try {
            con = this.conexion.getConnection();
            String sql = "select c.id, c.nombre, c.apellido, c.dni "
                    + "from cliente c "
                    + "order by c.nombre, c.apellido";
            sentencia = con.createStatement();

            rs = sentencia.executeQuery(sql);

            int id;
            String nombreCli, apellidoCli, dniCli;
            ClienteDto cliente;

            while (rs.next()) {
                id = rs.getInt("id");
                nombreCli = rs.getString("nombre");
                apellidoCli = rs.getString("apellido");
                dniCli = rs.getString("dni");
                cliente = new ClienteDto(id, nombreCli, apellidoCli, dniCli);
                lista.add(cliente);
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                rs.close();
                sentencia.close();
                this.conexion.cerrar();
            } catch (Exception ex) {

            }
        }
        return lista;
    }

    @Override
    public boolean insertar(ClienteDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean modificar(ClienteDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean borrar(ClienteDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
