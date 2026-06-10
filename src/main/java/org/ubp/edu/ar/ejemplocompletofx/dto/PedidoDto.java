/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * @author agustin
 */
public class PedidoDto {

    private int id;
    private int nro;
    private Date fecha;
    private Date fechaEstimada;
    private Date fechaEntrega;
    private ClienteDto cliente;
    private VendedorDto vendedor;
    private DistribuidorDto distribuidor;
    private String estado;
    private String observacion;
    private List<DetallePedidoDto> detalles;

    public PedidoDto() {
    }

    public PedidoDto(int id, int nro, Date fecha, ClienteDto cliente, VendedorDto vendedor, List<DetallePedidoDto> detalles) {
        this.id = id;
        this.nro = nro;
        this.fecha = fecha;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.detalles = detalles;
    }   
    
    public PedidoDto(int nro) {
        this.nro = nro;
    }

    public PedidoDto(int id, int nro, Date fecha, ClienteDto cliente, VendedorDto vendedor) {
        this.id = id;
        this.nro = nro;
        this.fecha = fecha;
        this.cliente = cliente;
        this.vendedor = vendedor;
        detalles = new ArrayList<>();
    }
    
    public PedidoDto(int id, int nro) {
        this.id = id;
        this.nro = nro;
    }

    public PedidoDto(int id, int nro, Date fecha) {
        this.id = id;
        this.nro = nro;
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNro() {
        return nro;
    }

    public void setNro(int nro) {
        this.nro = nro;
    }

    public ClienteDto getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDto cliente) {
        this.cliente = cliente;
    }

    public VendedorDto getVendedor() {
        return vendedor;
    }

    public void setVendedor(VendedorDto vendedor) {
        this.vendedor = vendedor;
    }

    public List<DetallePedidoDto> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedidoDto> detalles) {
        this.detalles = detalles;
    }

    public Date getFechaEstimada() {
        return fechaEstimada;
    }

    public void setFechaEstimada(Date fechaEstimada) {
        this.fechaEstimada = fechaEstimada;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public DistribuidorDto getDistribuidor() {
        return distribuidor;
    }

    public void setDistribuidor(DistribuidorDto distribuidor) {
        this.distribuidor = distribuidor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
       
    
}
