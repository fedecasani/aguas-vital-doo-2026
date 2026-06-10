package org.ubp.edu.ar.ejemplocompletofx.dto;

public class ZonaDto {

    private int id;
    private String nombre;

    public ZonaDto() {
    }

    public ZonaDto(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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
}
