package org.ubp.edu.ar.ejemplocompletofx.modelo;

public class Distribuidor extends Modelo {

    private int id;
    private String legajo;
    private String nombre;
    private String apellido;
    private int capacidadDiaria;
    private Zona zona;

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
        if (capacidadDiaria <= 0) {
            throw new IllegalArgumentException("La capacidad diaria debe ser mayor a cero");
        }
        this.capacidadDiaria = capacidadDiaria;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    @Override
    public String toString() {
        return legajo + " - " + nombre + " " + apellido;
    }
}
