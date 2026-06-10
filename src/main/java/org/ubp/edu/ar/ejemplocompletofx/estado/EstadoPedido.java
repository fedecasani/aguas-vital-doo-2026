package org.ubp.edu.ar.ejemplocompletofx.estado;

import java.util.Date;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Pedido;

public interface EstadoPedido {

    String getNombre();

    void registrarEntrega(Pedido pedido, Date fechaHoraEntrega);

    void mantenerPendiente(Pedido pedido, String motivo);

    void cancelar(Pedido pedido, String motivo);
}
