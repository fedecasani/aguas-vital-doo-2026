package org.ubp.edu.ar.ejemplocompletofx.modelo;

public class Zona extends Modelo {

    private int id;
    private String nombre;
    private Distribuidor distribuidor;

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

    public Distribuidor getDistribuidor() {
        return distribuidor;
    }

    public void setDistribuidor(Distribuidor distribuidor) {
        this.distribuidor = distribuidor;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
