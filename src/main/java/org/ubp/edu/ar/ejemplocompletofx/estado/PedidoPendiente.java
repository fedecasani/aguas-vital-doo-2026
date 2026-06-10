package org.ubp.edu.ar.ejemplocompletofx.estado;

import java.util.Date;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Pedido;

public class PedidoPendiente implements EstadoPedido {

    @Override
    public String getNombre() {
        return "PENDIENTE";
    }

    @Override
    public void registrarEntrega(Pedido pedido, Date fechaHoraEntrega) {
        if (fechaHoraEntrega == null) {
            throw new IllegalArgumentException("La fecha de entrega es obligatoria");
        }
        pedido.establecerDatosEntrega(fechaHoraEntrega, null);
        pedido.cambiarEstado(new PedidoEntregado());
    }

    @Override
    public void mantenerPendiente(Pedido pedido, String motivo) {
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar el motivo");
        }
        pedido.establecerDatosEntrega(null, motivo.trim());
    }

    @Override
    public void cancelar(Pedido pedido, String motivo) {
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar el motivo");
        }
        pedido.establecerDatosEntrega(null, motivo.trim());
        pedido.cambiarEstado(new PedidoCancelado());
    }
}
