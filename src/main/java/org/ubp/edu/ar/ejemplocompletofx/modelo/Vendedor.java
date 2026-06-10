/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.modelo;

import java.util.Arrays;
import java.util.List;
import org.ubp.edu.ar.ejemplocompletofx.dao.Dao;
import org.ubp.edu.ar.ejemplocompletofx.dao.VendedorDao;
import org.ubp.edu.ar.ejemplocompletofx.dto.VendedorDto;

/**
 *
 * @author agustin
 */
public class Vendedor extends Modelo {
    
    private String nombre;
    private String apellido;
    private String legajo;

    public Vendedor() {
        this.dao = new VendedorDao();
    }

    public List<Vendedor> listarTodos() {
        List<VendedorDto> vendedoresDto = this.dao.listarTodos();
        List<Vendedor> vendedores = Arrays.asList(this.mapper.map(vendedoresDto, Vendedor[].class));
        return vendedores;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    @Override
    public String toString() {
        return "Leg: " + legajo + ". " + nombre.toUpperCase() + " " + apellido.toUpperCase();
    }
}
