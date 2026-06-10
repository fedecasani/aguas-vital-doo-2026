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
import org.ubp.edu.ar.ejemplocompletofx.dto.VendedorDto;

/**
 *
 * @author agustin
 */
public class VendedorDao implements Dao<VendedorDto> {

    private ConexionSql conexion;

    @Override
    public VendedorDto buscar(VendedorDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<VendedorDto> listarPorCriterio(VendedorDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<VendedorDto> listarTodos() {
        this.conexion = new ConexionSql();
        Connection con;
        Statement sentencia = null;
        ResultSet rs = null;
        List<VendedorDto> lista = new ArrayList<>();

        try {
            con = this.conexion.getConnection();
            String sql = "select v.id, v.nombre, v.apellido, v.legajo "
                    + "from vendedor v "
                    + "order by v.nombre, v.apellido";
            sentencia = con.createStatement();

            rs = sentencia.executeQuery(sql);

            int id;
            String nombreVen, apellidoVen, legajoVen;
            VendedorDto vendedor;

            while (rs.next()) {
                id = rs.getInt("id");
                nombreVen = rs.getString("nombre");
                apellidoVen = rs.getString("apellido");
                legajoVen = rs.getString("legajo");
                vendedor = new VendedorDto(id, nombreVen, apellidoVen, legajoVen);
                lista.add(vendedor);
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
    public boolean insertar(VendedorDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean modificar(VendedorDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean borrar(VendedorDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
