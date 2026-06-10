package org.ubp.edu.ar.ejemplocompletofx.modelo;

import org.ubp.edu.ar.ejemplocompletofx.dto.DistribuidorDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.ZonaDto;
import org.ubp.edu.ar.ejemplocompletofx.factories.FabricaDao;

public class Distribuidor extends Modelo {

    private int id;
    private String legajo;
    private String nombre;
    private String apellido;
    private int capacidadDiaria;
    private Zona zona;

    public Distribuidor() {
        this.dao = FabricaDao.fabricar("DistribuidorDao");
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

    DistribuidorDto aDto() {
        return new DistribuidorDto(id, legajo, nombre, apellido, capacidadDiaria,
                new ZonaDto(zona.getId(), zona.getNombre()));
    }

    static Distribuidor desdeDto(DistribuidorDto dto) {
        Distribuidor distribuidor = new Distribuidor();
        distribuidor.setId(dto.getId());
        distribuidor.setLegajo(dto.getLegajo());
        distribuidor.setNombre(dto.getNombre());
        distribuidor.setApellido(dto.getApellido());
        distribuidor.setCapacidadDiaria(dto.getCapacidadDiaria());
        Zona zona = new Zona();
        zona.setId(dto.getZona().getId());
        zona.setNombre(dto.getZona().getNombre());
        distribuidor.setZona(zona);
        return distribuidor;
    }
}
