package org.ubp.edu.ar.ejemplocompletofx.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClienteTest {

    @Test
    void obtieneLaZonaDesdeElBarrioDelDomicilio() {
        Zona zona = new Zona();
        zona.setNombre("Zona Norte");
        Barrio barrio = new Barrio();
        barrio.setNombre("Cerro de las Rosas");
        barrio.setZona(zona);
        Cliente cliente = new Cliente();
        cliente.setBarrio(barrio);

        assertEquals(zona, cliente.getZona());
    }
}
