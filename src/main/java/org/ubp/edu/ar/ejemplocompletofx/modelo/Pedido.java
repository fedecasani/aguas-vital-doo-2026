package org.ubp.edu.ar.ejemplocompletofx.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.ubp.edu.ar.ejemplocompletofx.dao.PedidoDao;
import org.ubp.edu.ar.ejemplocompletofx.dto.DetallePedidoDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.PagoDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.PedidoDto;
import org.ubp.edu.ar.ejemplocompletofx.estado.EstadoPedido;
import org.ubp.edu.ar.ejemplocompletofx.estado.PedidoCancelado;
import org.ubp.edu.ar.ejemplocompletofx.estado.PedidoEntregado;
import org.ubp.edu.ar.ejemplocompletofx.estado.PedidoPendiente;
import org.ubp.edu.ar.ejemplocompletofx.factories.FabricaDao;

public class Pedido extends Modelo {

    private int id;
    private int nro;
    private Date fecha;
    private Cliente cliente;
    private Distribuidor distribuidor;
    private List<DetallePedido> detalles = new ArrayList<>();
    private Date fechaHoraEstimada;
    private Date fechaHoraEntrega;
    private String observacion;
    private EstadoPedido estado = new PedidoPendiente();

    public Pedido() {
        this.dao = FabricaDao.fabricar("PedidoDao");
    }

    public Pedido(int nro, Date fecha, Cliente cliente, Distribuidor distribuidor) {
        this();
        this.nro = nro;
        this.fecha = fecha;
        this.cliente = cliente;
        this.distribuidor = distribuidor;
    }

    public void prepararRegistro(Cliente cliente) {
        if (cliente == null || cliente.getZona() == null) {
            throw new IllegalArgumentException("El cliente y su zona son obligatorios");
        }
        this.cliente = cliente;
        this.fecha = new Date();
        PedidoDao pedidoDao = (PedidoDao) dao;
        var distribuidorDto = pedidoDao.buscarDistribuidorPorZona(cliente.getZona().getId());
        if (distribuidorDto == null) {
            throw new IllegalStateException("No hay distribuidor asignado a la zona");
        }
        this.distribuidor = Distribuidor.desdeDto(distribuidorDto);
        this.fechaHoraEstimada = pedidoDao.calcularFechaEstimada(
                distribuidor.aDto(), fecha);
    }

    public List<Pedido> listarTodos() {
        return desdeDtos(dao.listarTodos());
    }

    public List<Pedido> listarPorNro(int nro) {
        return desdeDtos(dao.listarPorCriterio(new PedidoDto(nro)));
    }

    public void buscarDetalles() {
        PedidoDto encontrado = (PedidoDto) dao.buscar(new PedidoDto(nro));
        if (encontrado != null) {
            this.detalles = mapearDetalles(encontrado.getDetalles());
        }
    }

    public boolean agregarItemDetallePedido(Producto producto, float cantidad, float precioVenta) {
        if (producto == null || cantidad <= 0 || precioVenta <= 0) {
            throw new IllegalArgumentException("Producto, cantidad y precio deben ser validos");
        }
        Optional<DetallePedido> existente = detalles.stream()
                .filter(detalle -> detalle.getProducto().getCodBarra()
                .equals(producto.getCodBarra()))
                .findFirst();
        if (existente.isPresent()) {
            return false;
        }
        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(this);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioVta(precioVenta);
        detalles.add(detalle);
        return true;
    }

    public float calcularTotalDetalle() {
        return (float) detalles.stream()
                .mapToDouble(detalle -> detalle.getPrecioVta() * detalle.getCantidad())
                .sum();
    }

    public boolean guardar() {
        if (cliente == null || distribuidor == null || detalles.isEmpty()) {
            throw new IllegalStateException("El pedido no tiene todos los datos requeridos");
        }
        return dao.insertar(aDto());
    }

    public boolean registrarEntrega(Date fechaEntrega, FormaPago formaPago) {
        if (formaPago == null) {
            throw new IllegalArgumentException("La forma de pago es obligatoria");
        }
        estado.registrarEntrega(this, fechaEntrega);
        PagoDto pago = new PagoDto(fechaEntrega, formaPago, calcularTotalDetalle());
        return ((PedidoDao) dao).actualizarConPago(aDto(), pago);
    }

    public void registrarEntrega(Date fechaEntrega) {
        estado.registrarEntrega(this, fechaEntrega);
    }

    public void mantenerPendiente(String motivo) {
        estado.mantenerPendiente(this, motivo);
    }

    public void cancelar(String motivo) {
        estado.cancelar(this, motivo);
    }

    public boolean mantenerPendienteYGuardar(String motivo) {
        estado.mantenerPendiente(this, motivo);
        return dao.modificar(aDto());
    }

    public boolean cancelarYGuardar(String motivo) {
        estado.cancelar(this, motivo);
        return dao.borrar(aDto());
    }

    private PedidoDto aDto() {
        PedidoDto dto = new PedidoDto();
        dto.setId(id);
        dto.setNro(nro);
        dto.setFecha(fecha);
        dto.setFechaEstimada(fechaHoraEstimada);
        dto.setFechaEntrega(fechaHoraEntrega);
        dto.setCliente(cliente == null ? null : cliente.aDto());
        dto.setDistribuidor(distribuidor == null ? null : distribuidor.aDto());
        dto.setEstado(estado.getNombre());
        dto.setObservacion(observacion);
        List<DetallePedidoDto> detallesDto = new ArrayList<>();
        for (DetallePedido detalle : detalles) {
            detallesDto.add(new DetallePedidoDto(
                    0, dto, detalle.getProducto().aDto(),
                    detalle.getCantidad(), detalle.getPrecioVta()));
        }
        dto.setDetalles(detallesDto);
        return dto;
    }

    private List<Pedido> desdeDtos(List<PedidoDto> pedidosDto) {
        List<Pedido> pedidos = new ArrayList<>();
        for (PedidoDto dto : pedidosDto) {
            Pedido pedido = new Pedido();
            pedido.id = dto.getId();
            pedido.nro = dto.getNro();
            pedido.fecha = dto.getFecha();
            pedido.fechaHoraEstimada = dto.getFechaEstimada();
            pedido.fechaHoraEntrega = dto.getFechaEntrega();
            pedido.cliente = Cliente.desdeDto(dto.getCliente());
            pedido.distribuidor = Distribuidor.desdeDto(dto.getDistribuidor());
            pedido.observacion = dto.getObservacion();
            pedido.estado = estadoDesdeNombre(dto.getEstado());
            pedido.detalles = mapearDetalles(dto.getDetalles());
            pedidos.add(pedido);
        }
        return pedidos;
    }

    private static List<DetallePedido> mapearDetalles(List<DetallePedidoDto> detallesDto) {
        List<DetallePedido> detalles = new ArrayList<>();
        if (detallesDto == null) {
            return detalles;
        }
        for (DetallePedidoDto dto : detallesDto) {
            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(Producto.desdeDto(dto.getProducto()));
            detalle.setCantidad(dto.getCantidad());
            detalle.setPrecioVta(dto.getPrecioVta());
            detalles.add(detalle);
        }
        return detalles;
    }

    private EstadoPedido estadoDesdeNombre(String nombre) {
        if ("ENTREGADO".equals(nombre)) {
            return new PedidoEntregado();
        }
        if ("CANCELADO".equals(nombre)) {
            return new PedidoCancelado();
        }
        return new PedidoPendiente();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNro() {
        return nro;
    }

    public void setNro(int nro) {
        this.nro = nro;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Distribuidor getDistribuidor() {
        return distribuidor;
    }

    public void setDistribuidor(Distribuidor distribuidor) {
        this.distribuidor = distribuidor;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public Date getFechaHoraEstimada() {
        return fechaHoraEstimada;
    }

    public void setFechaHoraEstimada(Date fechaHoraEstimada) {
        this.fechaHoraEstimada = fechaHoraEstimada;
    }

    public Date getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }

    public String getObservacion() {
        return observacion;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public String getNombreEstado() {
        return estado.getNombre();
    }

    public void cambiarEstado(EstadoPedido estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        this.estado = estado;
    }

    public void establecerDatosEntrega(Date fechaEntrega, String observacion) {
        this.fechaHoraEntrega = fechaEntrega;
        this.observacion = observacion;
    }
}
