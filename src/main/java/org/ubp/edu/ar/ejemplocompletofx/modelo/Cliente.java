/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.modelo;

import java.util.ArrayList;
import java.util.List;
import org.ubp.edu.ar.ejemplocompletofx.dao.ClienteDao;
import org.ubp.edu.ar.ejemplocompletofx.dto.BarrioDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.ClienteDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.ZonaDto;
import org.ubp.edu.ar.ejemplocompletofx.factories.FabricaDao;

/**
 *
 * @author agustin
 */
public class Cliente extends Modelo {

    private int id;
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
        List<Cliente> clientes = new ArrayList<>();
        for (ClienteDto clienteDto : clientesDto) {
            clientes.add(desdeDto(clienteDto));
        }
        return clientes;
    }

    public Cliente buscar(String tipoDocumento, String nroDocumento) {
        ClienteDto criterio = new ClienteDto();
        criterio.setTipoDocumento(tipoDocumento);
        criterio.setDni(nroDocumento);
        ClienteDto encontrado = ((ClienteDao) this.dao).buscar(criterio);
        return encontrado == null ? null : desdeDto(encontrado);
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

    ClienteDto aDto() {
        ClienteDto dto = new ClienteDto();
        dto.setId(id);
        dto.setTipoDocumento(tipoDocumento);
        dto.setDni(dni);
        dto.setNombre(nombre);
        dto.setApellido(apellido);
        dto.setRazonSocial(razonSocial);
        dto.setDireccion(direccion);
        dto.setTelefono(telefono);
        dto.setActivo(activo);
        if (barrio != null) {
            ZonaDto zonaDto = new ZonaDto(barrio.getZona().getId(), barrio.getZona().getNombre());
            dto.setBarrio(new BarrioDto(barrio.getId(), barrio.getNombre(), zonaDto));
        }
        return dto;
    }

    static Cliente desdeDto(ClienteDto dto) {
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setTipoDocumento(dto.getTipoDocumento());
        cliente.setDni(dto.getDni());
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setRazonSocial(dto.getRazonSocial());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setActivo(dto.isActivo());
        if (dto.getBarrio() != null) {
            Zona zona = new Zona();
            zona.setId(dto.getBarrio().getZona().getId());
            zona.setNombre(dto.getBarrio().getZona().getNombre());
            Barrio barrio = new Barrio();
            barrio.setId(dto.getBarrio().getId());
            barrio.setNombre(dto.getBarrio().getNombre());
            barrio.setZona(zona);
            cliente.setBarrio(barrio);
        }
        return cliente;
    }

    @Override
    public String toString() {
        return (nombre + " " + apellido).trim().toUpperCase();
    }
}
