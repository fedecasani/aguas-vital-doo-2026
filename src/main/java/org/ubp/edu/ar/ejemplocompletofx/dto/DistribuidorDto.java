package org.ubp.edu.ar.ejemplocompletofx.dto;

public class DistribuidorDto {

    private int id;
    private String legajo;
    private String nombre;
    private String apellido;
    private int capacidadDiaria;
    private ZonaDto zona;

    public DistribuidorDto() {
    }

    public DistribuidorDto(int id, String legajo, String nombre, String apellido,
            int capacidadDiaria, ZonaDto zona) {
        this.id = id;
        this.legajo = legajo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.capacidadDiaria = capacidadDiaria;
        this.zona = zona;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
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

    public int getCapacidadDiaria() {
        return capacidadDiaria;
    }

    public void setCapacidadDiaria(int capacidadDiaria) {
        this.capacidadDiaria = capacidadDiaria;
    }

    public ZonaDto getZona() {
        return zona;
    }

    public void setZona(ZonaDto zona) {
        this.zona = zona;
    }
}
