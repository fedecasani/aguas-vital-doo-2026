/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.dto;


/**
 *
 * @author agustin
 */
public class DetallePedidoDto {
    private int id;
    private PedidoDto pedido;
    private ProductoDto producto;
    private float cantidad;
    private float precioVta;

    public DetallePedidoDto() {
    }

    public DetallePedidoDto(int id, PedidoDto pedido, ProductoDto producto, float cantidad, float precioVta) {
        this.id = id;
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioVta = precioVta;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PedidoDto getPedido() {
        return pedido;
    }

    public void setPedido(PedidoDto pedido) {
        this.pedido = pedido;
    }

    public ProductoDto getProducto() {
        return producto;
    }

    public void setProducto(ProductoDto producto) {
        this.producto = producto;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecioVta() {
        return precioVta;
    }

    public void setPrecioVta(float precioVta) {
        this.precioVta = precioVta;
    }
}
