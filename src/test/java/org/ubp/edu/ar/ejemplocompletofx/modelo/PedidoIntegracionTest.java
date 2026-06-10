package org.ubp.edu.ar.ejemplocompletofx.modelo;

import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PedidoIntegracionTest {

    @Test
    void registraYActualizaUnPedidoCompleto() {
        Cliente cliente = new Cliente().buscar("DNI", "20369875");
        Producto producto = new Producto().listarTodos().get(0);
        Pedido nuevoPedido = new Pedido();

        assertNotNull(cliente);
        nuevoPedido.prepararRegistro(cliente);
        assertNotNull(nuevoPedido.getDistribuidor());
        assertNotNull(nuevoPedido.getFechaHoraEstimada());
        assertTrue(nuevoPedido.agregarItemDetallePedido(producto, 2, producto.getPrecio()));
        assertTrue(nuevoPedido.guardar());

        List<Pedido> pedidos = new Pedido().listarTodos();
        Pedido registrado = pedidos.stream()
                .max(java.util.Comparator.comparingInt(Pedido::getNro))
                .orElseThrow();

        assertEquals("PENDIENTE", registrado.getNombreEstado());
        assertTrue(registrado.mantenerPendienteYGuardar("Cliente ausente"));

        registrado.buscarDetalles();
        assertTrue(registrado.registrarEntrega(new Date(), FormaPago.EFECTIVO));

        Pedido actualizado = new Pedido().listarPorNro(registrado.getNro()).get(0);
        assertEquals("ENTREGADO", actualizado.getNombreEstado());
        assertNotNull(actualizado.getFechaHoraEntrega());
    }
}
