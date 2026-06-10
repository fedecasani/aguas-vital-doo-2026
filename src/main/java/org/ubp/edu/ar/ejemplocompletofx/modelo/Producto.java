/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.modelo;

import java.util.Arrays;
import java.util.List;
import org.ubp.edu.ar.ejemplocompletofx.dao.Dao;
import org.ubp.edu.ar.ejemplocompletofx.dao.ProductoDao;
import org.ubp.edu.ar.ejemplocompletofx.dto.ProductoDto;

/**
 *
 * @author agustin
 */
public class Producto extends Modelo {
    private String nombre;
    private String codBarra;
    private float precio;
    private float cantidad;
    private TipoProducto tipo;
    private int capacidadLitros;

    public Producto() {
        this.dao = new ProductoDao();
    }
    
    public List<Producto> listarTodos() {
        List<ProductoDto> productosDto = this.dao.listarTodos();
        List<Producto> productos = Arrays.asList(this.mapper.map(productosDto, Producto[].class));
        return productos;
    }
    
    @Override
    public String toString() {
        return "Cod: " + codBarra + ". " + nombre.toUpperCase();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public TipoProducto getTipo() {
        return tipo;
    }

    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }

    public int getCapacidadLitros() {
        return capacidadLitros;
    }

    public void setCapacidadLitros(int capacidadLitros) {
        if (capacidadLitros <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero");
        }
        this.capacidadLitros = capacidadLitros;
    }
}
