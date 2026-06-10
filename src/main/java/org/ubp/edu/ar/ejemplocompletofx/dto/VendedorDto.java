/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.dto;

/**
 *
 * @author agustin
 */
public class VendedorDto {
    
    private int id;
    private String nombre;
    private String apellido;
    private String legajo;

    public VendedorDto() {
    }

    public VendedorDto(int id, String nombre, String apellido, String legajo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.legajo = legajo;
    }
 
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
