package org.ubp.edu.ar.ejemplocompletofx.controladores;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ubp.edu.ar.ejemplocompletofx.factories.FabricaModelo;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Cliente;
import org.ubp.edu.ar.ejemplocompletofx.modelo.DetallePedido;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Pedido;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Producto;

public class EditarPedidoController extends Controller implements Initializable {

    private Cliente clienteModelo;
    private Producto productoModelo;
    private Pedido pedido;
    private Controller parentController;
    private ObservableList<DetallePedido> detalles;

    @FXML
    private ComboBox<String> cmbTipoDocumento;
    @FXML
    private TextField txtDocumento;
    @FXML
    private Label lblCliente;
    @FXML
    private Label lblZona;
    @FXML
    private Label lblDistribuidor;
    @FXML
    private Label lblFechaEstimada;
    @FXML
    private ComboBox<Producto> cmbProducto;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtTotal;
    @FXML
    private TableView<DetallePedido> tableView;
    @FXML
    private Button btnGuardar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteModelo = (Cliente) FabricaModelo.fabricar("Cliente");
        productoModelo = (Producto) FabricaModelo.fabricar("Producto");
        pedido = (Pedido) FabricaModelo.fabricar("Pedido");
        detalles = FXCollections.observableArrayList();
        cmbTipoDocumento.setItems(FXCollections.observableArrayList("DNI", "CUIT"));
        cmbTipoDocumento.setValue("DNI");
        configurarTabla();
        tableView.setItems(detalles);
        cargarProductos();
        btnGuardar.setDisable(true);
    }

    public void passData(Controller parentController) {
        this.parentController = parentController;
    }

    private void cargarProductos() {
        progress.setVisible(true);
        List<Producto> productos = productoModelo.listarTodos();
        cmbProducto.setItems(FXCollections.observableArrayList(productos));
        progress.setVisible(false);
    }

    private void configurarTabla() {
        TableColumn<DetallePedido, String> productoCol = (TableColumn<DetallePedido, String>) tableView.getColumns().get(0);
        productoCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProducto().toString()));
        TableColumn<DetallePedido, Float> cantidadCol = (TableColumn<DetallePedido, Float>) tableView.getColumns().get(1);
        cantidadCol.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        TableColumn<DetallePedido, Float> precioCol = (TableColumn<DetallePedido, Float>) tableView.getColumns().get(2);
        precioCol.setCellValueFactory(new PropertyValueFactory<>("precioVta"));
        TableColumn<DetallePedido, Float> subtotalCol = (TableColumn<DetallePedido, Float>) tableView.getColumns().get(3);
        subtotalCol.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
    }

    @FXML
    private void buscarCliente() {
        String documento = txtDocumento.getText().trim();
        if (documento.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, null, "Registrar pedido", "Ingrese el numero de documento.");
            return;
        }
        try {
            Cliente cliente = clienteModelo.buscar(cmbTipoDocumento.getValue(), documento);
            if (cliente == null) {
                limpiarCliente();
                showAlert(Alert.AlertType.INFORMATION, null, "Registrar pedido",
                        "El cliente no esta registrado. Debe registrarlo antes de continuar.");
                return;
            }
            pedido.prepararRegistro(cliente);
            lblCliente.setText(cliente.toString());
            lblZona.setText(cliente.getZona().getNombre());
            lblDistribuidor.setText(pedido.getDistribuidor().toString());
            lblFechaEstimada.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm")
                    .format(pedido.getFechaHoraEstimada()));
            actualizarEstadoGuardar();
        } catch (RuntimeException ex) {
            limpiarCliente();
            showAlert(Alert.AlertType.ERROR, null, "Registrar pedido", ex.getMessage());
        }
    }

    @FXML
    private void alCambiarProducto() {
        Producto producto = cmbProducto.getValue();
        txtCantidad.clear();
        txtPrecio.clear();
        if (producto != null) {
            txtCantidad.setText("1");
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
        }
    }

    @FXML
    private void agregarItemDetalle() {
        Producto producto = cmbProducto.getValue();
        try {
            float cantidad = Float.parseFloat(txtCantidad.getText().trim());
            if (!pedido.agregarItemDetallePedido(producto, cantidad, producto.getPrecio())) {
                showAlert(Alert.AlertType.WARNING, null, "Registrar pedido",
                        "El producto ya se encuentra en el pedido.");
                return;
            }
            detalles.setAll(pedido.getDetalles());
            limpiarProducto();
            actualizarTotal();
            actualizarEstadoGuardar();
        } catch (NullPointerException | NumberFormatException ex) {
            showAlert(Alert.AlertType.WARNING, null, "Registrar pedido",
                    "Seleccione un producto e ingrese una cantidad valida.");
        } catch (IllegalArgumentException ex) {
            showAlert(Alert.AlertType.WARNING, null, "Registrar pedido", ex.getMessage());
        }
    }

    @FXML
    private void quitarItemDetalle() {
        DetallePedido detalle = tableView.getSelectionModel().getSelectedItem();
        if (detalle != null) {
            pedido.getDetalles().remove(detalle);
            detalles.setAll(pedido.getDetalles());
            actualizarTotal();
            actualizarEstadoGuardar();
        }
    }

    @FXML
    private void guardarPedido() {
        try {
            if (pedido.guardar()) {
                parentController.loadData();
                showAlert(Alert.AlertType.INFORMATION, null, "Registrar pedido",
                        "Pedido registrado correctamente.");
                btnGuardar.getScene().getWindow().hide();
            }
        } catch (RuntimeException ex) {
            showAlert(Alert.AlertType.ERROR, null, "Registrar pedido", ex.getMessage());
        }
    }

    private void limpiarCliente() {
        pedido = (Pedido) FabricaModelo.fabricar("Pedido");
        detalles.clear();
        lblCliente.setText("-");
        lblZona.setText("-");
        lblDistribuidor.setText("-");
        lblFechaEstimada.setText("-");
        actualizarTotal();
        actualizarEstadoGuardar();
    }

    private void limpiarProducto() {
        cmbProducto.setValue(null);
        txtCantidad.clear();
        txtPrecio.clear();
    }

    private void actualizarTotal() {
        txtTotal.setText(String.format("%.2f", pedido.calcularTotalDetalle()));
    }

    private void actualizarEstadoGuardar() {
        btnGuardar.setDisable(pedido.getCliente() == null || pedido.getDetalles().isEmpty());
    }
}
