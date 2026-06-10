/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.modelo;

import java.util.Arrays;
import java.util.List;
import org.ubp.edu.ar.ejemplocompletofx.dto.ClienteDto;
import org.ubp.edu.ar.ejemplocompletofx.factories.FabricaDao;

/**
 *
 * @author agustin
 */
public class Cliente extends Modelo {

    private String nombre;
    private String apellido;
    private String dni;
    private String tipoDocumento;
    private String razonSocial;
    private String direccion;
    private String telefono;
    private Barrio barrio;
    private boolean activo = true;

    public Cliente() {
        this.dao = FabricaDao.fabricar("ClienteDao");
    }

    public List<Cliente> listarTodos() {
        List<ClienteDto> clientesDto = this.dao.listarTodos();
        List<Cliente> clientes = Arrays.asList(this.mapper.map(clientesDto, Cliente[].class));
        return clientes;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Barrio getBarrio() {
        return barrio;
    }

    public void setBarrio(Barrio barrio) {
        this.barrio = barrio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Zona getZona() {
        return barrio != null ? barrio.getZona() : null;
    }

    @Override
    public String toString() {
        return nombre.toUpperCase() + " " + apellido.toUpperCase();
    }
}
