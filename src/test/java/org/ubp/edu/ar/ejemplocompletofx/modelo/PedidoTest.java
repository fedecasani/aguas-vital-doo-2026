package org.ubp.edu.ar.ejemplocompletofx.modelo;

import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PedidoTest {

    @Test
    void calculaElTotalConLosPreciosRegistradosEnElDetalle() {
        Pedido pedido = new Pedido();
        Producto agua = producto("AGUA-20", 3500);
        Producto soda = producto("SODA-1", 1200);

        pedido.agregarItemDetallePedido(agua, 2, agua.getPrecio());
        pedido.agregarItemDetallePedido(soda, 3, soda.getPrecio());

        assertEquals(10600, pedido.calcularTotalDetalle(), 0.01);
    }

    @Test
    void noPermiteAgregarDosVecesElMismoProducto() {
        Pedido pedido = new Pedido();
        Producto agua = producto("AGUA-12", 2800);

        pedido.agregarItemDetallePedido(agua, 1, agua.getPrecio());

        assertEquals(false, pedido.agregarItemDetallePedido(agua, 2, agua.getPrecio()));
    }

    @Test
    void unPedidoPendientePuedeRegistrarseComoEntregado() {
        Pedido pedido = new Pedido();
        Date entrega = new Date();

        pedido.registrarEntrega(entrega);

        assertEquals("ENTREGADO", pedido.getNombreEstado());
        assertEquals(entrega, pedido.getFechaHoraEntrega());
    }

    @Test
    void unPedidoEntregadoNoPuedeVolverAPendiente() {
        Pedido pedido = new Pedido();
        pedido.registrarEntrega(new Date());

        assertThrows(IllegalStateException.class,
                () -> pedido.mantenerPendiente("Cliente ausente"));
    }

    private Producto producto(String codigo, float precio) {
        Producto producto = new Producto();
        producto.setCodBarra(codigo);
        producto.setNombre(codigo);
        producto.setPrecio(precio);
        return producto;
    }
}
