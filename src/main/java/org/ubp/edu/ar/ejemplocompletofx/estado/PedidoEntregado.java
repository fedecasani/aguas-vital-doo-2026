package org.ubp.edu.ar.ejemplocompletofx.estado;

import java.util.Date;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Pedido;

public class PedidoEntregado implements EstadoPedido {

    @Override
    public String getNombre() {
        return "ENTREGADO";
    }

    @Override
    public void registrarEntrega(Pedido pedido, Date fechaHoraEntrega) {
        throw new IllegalStateException("El pedido ya fue entregado");
    }

    @Override
    public void mantenerPendiente(Pedido pedido, String motivo) {
        throw new IllegalStateException("Un pedido entregado no puede volver a pendiente");
    }

    @Override
    public void cancelar(Pedido pedido, String motivo) {
        throw new IllegalStateException("Un pedido entregado no puede cancelarse");
    }
}
