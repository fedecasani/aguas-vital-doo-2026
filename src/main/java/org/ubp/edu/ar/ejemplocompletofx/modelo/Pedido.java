/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.modelmapper.ModelMapper;
import org.ubp.edu.ar.ejemplocompletofx.dao.ConexionSql;
import org.ubp.edu.ar.ejemplocompletofx.dao.Dao;
import org.ubp.edu.ar.ejemplocompletofx.dao.PedidoDao;
import org.ubp.edu.ar.ejemplocompletofx.dto.PedidoDto;
import org.ubp.edu.ar.ejemplocompletofx.factories.FabricaDao;
import org.ubp.edu.ar.ejemplocompletofx.estado.EstadoPedido;
import org.ubp.edu.ar.ejemplocompletofx.estado.PedidoPendiente;

/**
 *
 * @author agustin
 */
public class Pedido extends Modelo {
    private int nro;
    private Date fecha;
    private Cliente cliente;
    private Vendedor vendedor;
    private List<DetallePedido> detalles = new ArrayList<>();
    private Date fechaHoraEstimada;
    private Date fechaHoraEntrega;
    private String observacion;
    private EstadoPedido estado = new PedidoPendiente();

    public Pedido() {
        this.dao = FabricaDao.fabricar("PedidoDao");
    }

    public Pedido(int nro, Date fecha, Cliente cliente, Vendedor vendedor) {
        this.dao = new PedidoDao();
        this.nro = nro;
        this.fecha = fecha;
        this.cliente = cliente;
        this.vendedor = vendedor;
        detalles = new ArrayList<>();
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

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
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

    public void registrarEntrega(Date fechaHoraEntrega) {
        estado.registrarEntrega(this, fechaHoraEntrega);
    }

    public void mantenerPendiente(String motivo) {
        estado.mantenerPendiente(this, motivo);
    }

    public void cancelar(String motivo) {
        estado.cancelar(this, motivo);
    }

    public void cambiarEstado(EstadoPedido estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        this.estado = estado;
    }

    public void establecerDatosEntrega(Date fechaHoraEntrega, String observacion) {
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.observacion = observacion;
    }

    public ModelMapper getMapper() {
        return mapper;
    }

    public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public List<Pedido> listarTodos() {
        List<PedidoDto> pedidosDto = this.dao.listarTodos();
        List<Pedido> pedidos = Arrays.asList(this.mapper.map(pedidosDto, Pedido[].class));
        return pedidos;
    }

    public List<Pedido> listarPorNro(int nro) {
        List<PedidoDto> pedidosDto = this.dao.listarPorCriterio(new PedidoDto(nro));
        List<Pedido> pedidos = Arrays.asList(this.mapper.map(pedidosDto, Pedido[].class));
        return pedidos;
    }

    public void buscarDetalles() {
        PedidoDto pedidoDto = mapper.map(this, PedidoDto.class);
        pedidoDto = (PedidoDto) this.dao.buscar(pedidoDto);
        List<DetallePedido> dets = new ArrayList<>(Arrays.asList(this.mapper.map(pedidoDto.getDetalles(), DetallePedido[].class)));
        this.detalles = dets;
    }

    public boolean agregarItemDetallePedido(Producto prod, float cantidad, float precVta) {
        DetallePedido det = new DetallePedido();
        det.setCantidad(cantidad);
        det.setPedido(this);
        det.setPrecioVta(precVta);
        det.setProducto(prod);
        Optional<DetallePedido> detOpt = this.detalles
                .stream()
                .filter(x -> x.getProducto().getCodBarra().equals(prod.getCodBarra())).findFirst();
        //Si no esta presente en la lista entonces agrego el detalle
        if (!detOpt.isPresent()) {
            this.detalles.add(det);
            return true;
        }
        return false;
    }

    public float calcularTotalDetalle() {
        Double total = this.detalles.stream()
                .mapToDouble(det -> (det.getPrecioVta() * det.getCantidad()))
                .sum();
        return total.floatValue();
    }

    public boolean guardar() {
        PedidoDto pedidoDto = this.mapper.map(this, PedidoDto.class);
        return this.dao.insertar(pedidoDto);
    }

    public boolean modificar() {
        PedidoDto pedidoDto = this.mapper.map(this, PedidoDto.class);
        return this.dao.modificar(pedidoDto);
    }

    public boolean eliminar() {
        PedidoDto pedidoDto = this.mapper.map(this, PedidoDto.class);
        return this.dao.borrar(pedidoDto);
    }

    public void generarReporte() throws FileNotFoundException, JRException {
        String path = getClass().getResource("/rpt_pedidos.jasper").getPath();
        InputStream reportStream = new FileInputStream(new File(path));
//            JasperReport report = JasperCompileManager.compileReport(reportStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, null, new ConexionSql().getConnection());
        JasperViewer.viewReport(jasperPrint, false);
    }

}
