/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.ubp.edu.ar.ejemplocompletofx.dto.ClienteDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.DetallePedidoDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.PedidoDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.ProductoDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.VendedorDto;

/**
 *
 * @author agustin
 */
public class PedidoDao implements Dao<PedidoDto> {

    private ConexionSql conexion;

    @Override
    public PedidoDto buscar(PedidoDto dto) {
        this.conexion = new ConexionSql();
        Connection con;
        PreparedStatement sentencia = null;
        ResultSet rs = null;

        try {
            List<DetallePedidoDto> listaDet = new ArrayList<>();
            con = this.conexion.getConnection();
            String sql = "select d.id as idDetPed, d.cantidad, d.precioVta, "
                    + "p.id as idProd, p.nombre, p.codBarra, p.precio, p.cantidad "
                    + "from pedido pe, detallepedido d, producto p "
                    + "where pe.id = d.idPedido "
                    + "and p.id = d.idProducto "
                    + "and pe.nro = ?";
            sentencia = con.prepareStatement(sql);
            sentencia.setInt(1, dto.getNro());

            rs = sentencia.executeQuery();

            int id, idProd;
            String nombreProd, codBarraProd;
            float precioVtaDet, cantDet, precioProd, cantProd;
            DetallePedidoDto det;

            while (rs.next()) {
                id = rs.getInt("idDetPed");
                cantDet = rs.getFloat("cantidad");
                precioVtaDet = rs.getFloat("precioVta");
                idProd = rs.getInt("idProd");
                nombreProd = rs.getString("nombre");
                codBarraProd = rs.getString("codBarra");
                precioProd = rs.getFloat("precio");
                cantProd = rs.getFloat("cantidad");

                det = new DetallePedidoDto(id, dto,
                        new ProductoDto(idProd, nombreProd, codBarraProd, precioProd, cantProd),
                        cantDet, precioVtaDet);
                listaDet.add(det);
            }
            dto.setDetalles(listaDet);
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
        return dto;
    }

    @Override
    public List<PedidoDto> listarPorCriterio(PedidoDto dto) {
        this.conexion = new ConexionSql();
        Connection con;
        PreparedStatement sentencia = null;
        ResultSet rs = null;
        List<PedidoDto> lista = new ArrayList<>();

        try {
            con = this.conexion.getConnection();
            String sql = "select p.id as idPed, p.nro, p.fecha, "
                    + "c.id as idCli, c.nombre, c.apellido, c.dni, "
                    + "v.id as idVen, v.nombre as nomVen, v.apellido as apeVen, v.legajo "
                    + "from pedido p, cliente c, vendedor v "
                    + "where p.idCliente = c.id "
                    + "and p.idVendedor = v.id "
                    + "and p.nro = ? "
                    + "order by p.nro, p.fecha";
            sentencia = con.prepareStatement(sql);
            sentencia.setInt(1, dto.getNro());

            rs = sentencia.executeQuery();

            int id, nro, idCli, idVend;
            Date fecha;
            String nombreCli, apellidoCli, dniCli;
            String nombreVen, apellidoVen, legajoVen;
            PedidoDto pedido;

            while (rs.next()) {
                id = rs.getInt("idPed");
                nro = rs.getInt("nro");
                fecha = rs.getDate("fecha");
                idCli = rs.getInt("idCli");
                nombreCli = rs.getString("nombre");
                apellidoCli = rs.getString("apellido");
                dniCli = rs.getString("dni");
                idVend = rs.getInt("idVen");
                nombreVen = rs.getString("nomVen");
                apellidoVen = rs.getString("apeVen");
                legajoVen = rs.getString("legajo");
                pedido = new PedidoDto(id, nro, fecha,
                        new ClienteDto(idCli, nombreCli, apellidoCli, dniCli),
                        new VendedorDto(idVend, nombreVen, apellidoVen, legajoVen));
                lista.add(pedido);
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
    public List<PedidoDto> listarTodos() {
        this.conexion = new ConexionSql();
        Connection con;
        Statement sentencia = null;
        ResultSet rs = null;
        List<PedidoDto> lista = new ArrayList<>();

        try {
            con = this.conexion.getConnection();
            String sql = "select p.id as idPedido, p.nro, p.fecha, "
                    + "c.id as idCliente, c.nombre as nombreCli, c.apellido as apeCli, c.dni, "
                    + "v.id as idVendedor, v.nombre as nombreVen, v.apellido as apeVen, v.legajo "
                    + "from pedido p, cliente c, vendedor v "
                    + "where p.idCliente = c.id "
                    + "and p.idVendedor = v.id "
                    + "order by p.nro, p.fecha";
            sentencia = con.createStatement();

            rs = sentencia.executeQuery(sql);

            int id, nro, idCli, idVend;
            Date fecha;
            String nombreCli, apellidoCli, dniCli;
            String nombreVen, apellidoVen, legajoVen;
            PedidoDto pedido;

            while (rs.next()) {
                id = rs.getInt("idPedido");
                nro = rs.getInt("nro");
                fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("fecha"));
                idCli = rs.getInt("idCliente");
                nombreCli = rs.getString("nombreCli");
                apellidoCli = rs.getString("apeCli");
                dniCli = rs.getString("dni");
                idVend = rs.getInt("idVendedor");
                nombreVen = rs.getString("nombreVen");
                apellidoVen = rs.getString("apeVen");
                legajoVen = rs.getString("legajo");
                pedido = new PedidoDto(id, nro, fecha,
                        new ClienteDto(idCli, nombreCli, apellidoCli, dniCli),
                        new VendedorDto(idVend, nombreVen, apellidoVen, legajoVen));
                lista.add(pedido);
            }

        } catch (SQLException | ParseException e) {
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
    public boolean insertar(PedidoDto dto) {
        this.conexion = new ConexionSql();
        Connection con = null;
        PreparedStatement sentencia = null;

        try {
            con = this.conexion.getConnection();
            con.setAutoCommit(false);
            String sql = "insert into pedido (fecha, idCliente, idVendedor, nro) "
                    + "values(?,(select c.id from cliente c where c.dni like ?), "
                    + "(select v.id from vendedor v where v.legajo like ?), "
                    + "(select (max(p.nro)+1) from pedido p))";
            sentencia = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getFecha()));
            sentencia.setString(2, dto.getCliente().getDni());
            sentencia.setString(3, dto.getVendedor().getLegajo());

            int resultado = sentencia.executeUpdate();

            if (resultado <= 0) {
                con.rollback();
                return false;
            }

            //obtengo las ultimas PK generada
            ResultSet rs = sentencia.getGeneratedKeys();
            int idPedidoUltimo = 0;
            while (rs.next()) {
                idPedidoUltimo = rs.getInt(1);
            }

            for (DetallePedidoDto detalle : dto.getDetalles()) {
                sql = "insert into detallepedido (idPedido, idProducto, cantidad, precioVta) "
                        + "values(?,(select p.id from producto p where p.codBarra like ?),?,?)";
                sentencia = con.prepareStatement(sql);
                sentencia.setInt(1, idPedidoUltimo);
                sentencia.setString(2, detalle.getProducto().getCodBarra());
                sentencia.setFloat(3, detalle.getCantidad());
                sentencia.setFloat(4, detalle.getPrecioVta());
                resultado = sentencia.executeUpdate();
                if (resultado <= 0) {
                    con.rollback();
                    return false;
                }
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {

                }
            }
            return false;
        } finally {
            try {
                sentencia.close();
                this.conexion.cerrar();
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }

    @Override
    public boolean modificar(PedidoDto dto) {
        this.conexion = new ConexionSql();
        Connection con = null;
        PreparedStatement sentencia = null;

        try {
            con = this.conexion.getConnection();
            con.setAutoCommit(false);
            String sql = "update pedido set fecha=?, "
                    + "idCliente=(select c.id from cliente c where c.dni like ?), "
                    + "idVendedor=(select v.id from vendedor v where v.legajo like ?) "
                    + "where nro=?";
            sentencia = con.prepareStatement(sql);
            sentencia.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getFecha()));
            sentencia.setString(2, dto.getCliente().getDni());
            sentencia.setString(3, dto.getVendedor().getLegajo());
            sentencia.setInt(4, dto.getNro());

            int resultado = sentencia.executeUpdate();

            if (resultado <= 0) {
                con.rollback();
                return false;
            }

            sql = "delete from detallepedido "
                    + "where idPedido=(select p1.id from pedido p1 where p1.nro=?)";

            sentencia = con.prepareStatement(sql);
            sentencia.setInt(1, dto.getNro());
            sentencia.executeUpdate();

            for (DetallePedidoDto detalle : dto.getDetalles()) {
                sql = "insert into detallepedido (idPedido, idProducto, cantidad, precioVta) "
                        + "values((select p1.id from pedido p1 where p1.nro=?), "
                        + "(select p.id from producto p where p.codBarra like ?),?,?)";
                sentencia = con.prepareStatement(sql);
                sentencia.setInt(1, detalle.getPedido().getNro());
                sentencia.setString(2, detalle.getProducto().getCodBarra());
                sentencia.setFloat(3, detalle.getCantidad());
                sentencia.setFloat(4, detalle.getPrecioVta());
                resultado = sentencia.executeUpdate();
                if (resultado <= 0) {
                    con.rollback();
                    return false;
                }
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {

                }
            }
            return false;
        } finally {
            try {
                sentencia.close();
                this.conexion.cerrar();
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }

    @Override
    public boolean borrar(PedidoDto dto) {
        this.conexion = new ConexionSql();
        Connection con = null;
        PreparedStatement sentencia = null;

        try {
            con = this.conexion.getConnection();
            con.setAutoCommit(false);
            String sql = "delete from detallepedido "
                    + "where idPedido=(select p1.id from pedido p1 where p1.nro=?)";
            sentencia = con.prepareStatement(sql);
            sentencia.setInt(1, dto.getNro());
            int resultado = sentencia.executeUpdate();

            if (resultado <= 0) {
                con.rollback();
                return false;
            }

            sql = "delete from pedido "
                    + "where nro=?";
            sentencia = con.prepareStatement(sql);
            sentencia.setInt(1, dto.getNro());
            resultado = sentencia.executeUpdate();
            if (resultado <= 0) {
                con.rollback();
                return false;
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {

                }
            }
            return false;
        } finally {
            try {
                sentencia.close();
                this.conexion.cerrar();
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }

}
