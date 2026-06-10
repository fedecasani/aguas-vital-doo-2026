package org.ubp.edu.ar.ejemplocompletofx.dto;

public class BarrioDto {

    private int id;
    private String nombre;
    private ZonaDto zona;

    public BarrioDto() {
    }

    public BarrioDto(int id, String nombre, ZonaDto zona) {
        this.id = id;
        this.nombre = nombre;
        this.zona = zona;
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

    public ZonaDto getZona() {
        return zona;
    }

    public void setZona(ZonaDto zona) {
        this.zona = zona;
    }
}
