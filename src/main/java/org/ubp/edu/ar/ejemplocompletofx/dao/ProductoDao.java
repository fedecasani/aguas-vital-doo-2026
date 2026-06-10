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
import org.ubp.edu.ar.ejemplocompletofx.dto.ProductoDto;

/**
 *
 * @author agustin
 */
public class ProductoDao implements Dao<ProductoDto> {

    private ConexionSql conexion;
    
    @Override
    public ProductoDto buscar(ProductoDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ProductoDto> listarPorCriterio(ProductoDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ProductoDto> listarTodos() {
        this.conexion = new ConexionSql();
        Connection con;
        Statement sentencia = null;
        ResultSet rs = null;
        List<ProductoDto> lista = new ArrayList<>();
        
        try {
            con = this.conexion.getConnection();
            String sql = "select p.id, p.nombre, p.codBarra, p.precio, p.cantidad "
                    + "from producto p "
                    + "order by p.nombre, p.codBarra";
            sentencia = con.createStatement();

            rs = sentencia.executeQuery(sql);

            int id;
            String nombre, codBarra;
            float precio, cantidad;
            ProductoDto producto;

            while (rs.next()) {
                id = rs.getInt("id");
                nombre = rs.getString("nombre");
                codBarra = rs.getString("codBarra");
                precio = rs.getFloat("precio");
                cantidad = rs.getFloat("cantidad");
                producto = new ProductoDto(id, nombre, codBarra, precio, cantidad);
                lista.add(producto);
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
    public boolean insertar(ProductoDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean modificar(ProductoDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean borrar(ProductoDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
