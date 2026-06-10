package org.ubp.edu.ar.ejemplocompletofx.estado;

import java.util.Date;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Pedido;

public class PedidoCancelado implements EstadoPedido {

    @Override
    public String getNombre() {
        return "CANCELADO";
    }

    @Override
    public void registrarEntrega(Pedido pedido, Date fechaHoraEntrega) {
        throw new IllegalStateException("Un pedido cancelado no puede entregarse");
    }

    @Override
    public void mantenerPendiente(Pedido pedido, String motivo) {
        throw new IllegalStateException("Un pedido cancelado no puede volver a pendiente");
    }

    @Override
    public void cancelar(Pedido pedido, String motivo) {
        throw new IllegalStateException("El pedido ya fue cancelado");
    }
}
