/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.controladores;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import net.synedra.validatorfx.Validator;
import org.ubp.edu.ar.ejemplocompletofx.factories.FabricaModelo;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Cliente;
import org.ubp.edu.ar.ejemplocompletofx.modelo.DetallePedido;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Pedido;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Producto;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Vendedor;

/**
 * FXML Controller class
 *
 * @author agustin
 */
public class EditarPedidoController extends Controller implements Initializable {

    private Vendedor vendedor;
    private Cliente cliente;
    private Producto producto;
    private Pedido pedido;
    @FXML
    private TextField txtNro;
    @FXML
    private DatePicker txtFecha;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtTotal;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnAgregarItem;
    @FXML
    private Button btnQuitarItem;
    @FXML
    private Button btnCerrarEditarPedido;
    @FXML
    private TableView<DetallePedido> tableView;
    private ObservableList<DetallePedido> datos = null;
    @FXML
    private ComboBox<Cliente> cmbCliente;
    private ObservableList<Cliente> datosCmbCliente = null;
    @FXML
    private ComboBox<Vendedor> cmbVendedor;
    private ObservableList<Vendedor> datosCmbVendedor = null;
    @FXML
    private ComboBox<Producto> cmbProducto;
    private ObservableList<Producto> datosCmbProducto = null;
    private Controller otherCtrl;
    private Validator validador;

    //-----------------------------------
    //MENSAJE 9 DEL DIAGRAMA DE SECUENCIA
    //-----------------------------------
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cliente = (Cliente) FabricaModelo.fabricar("Cliente");
        vendedor = (Vendedor) FabricaModelo.fabricar("Vendedor");
        producto = (Producto) FabricaModelo.fabricar("Producto");
        this.txtPrecio.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d+\\.\\d+")) {
                txtPrecio.setText(newValue.replaceAll("[^\\d+\\.\\d+]", ""));
            }
        });
        //https://github.com/effad/ValidatorFX
        this.validador = new Validator();
        this.validador.createCheck()
                .dependsOn("precioVta", this.txtPrecio.textProperty())
                .withMethod(context -> {
                    String texto = context.get("precioVta");
                    if (texto == null || texto.isEmpty()) {
                        context.error("Campo requerido");
                    }
                })
                .decorates(this.txtPrecio)
                .immediate();
        this.txtCantidad.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d+\\.\\d+")) {
                txtCantidad.setText(newValue.replaceAll("[^\\d+\\.\\d+]", ""));
            }
        });
        this.validador.createCheck()
                .dependsOn("cantidad", this.txtCantidad.textProperty())
                .withMethod(context -> {
                    String texto = context.get("cantidad");
                    if (texto == null || texto.isEmpty()) {
                        context.error("Campo requerido");
                    }
                })
                .decorates(this.txtCantidad)
                .immediate();
        if (this.tableView != null) {
            this.configureTable();
            this.loadData();
            this.btnGuardar.setDisable(false);
            if (this.tableView.getItems().isEmpty()) {
                this.btnGuardar.setDisable(true);
            }
        }
    }

    //-----------------------------------
    //MENSAJE 10 DEL DIAGRAMA DE SECUENCIA
    //-----------------------------------
    @Override
    public void loadData() {
        this.progress.setVisible(true);
        //-----------------------------------
        //MENSAJE 11 DEL DIAGRAMA DE SECUENCIA
        //-----------------------------------
        List<Producto> productos = this.producto.listarTodos();

        //-----------------------------------
        //MENSAJE 13 DEL DIAGRAMA DE SECUENCIA
        //-----------------------------------
        List<Cliente> clientes = this.cliente.listarTodos();

        //-----------------------------------
        //MENSAJE 15 DEL DIAGRAMA DE SECUENCIA
        //-----------------------------------
        List<Vendedor> vendedores = this.vendedor.listarTodos();

        this.datosCmbCliente = FXCollections.observableList(clientes);
        this.cmbCliente.setItems(this.datosCmbCliente);
        this.datosCmbVendedor = FXCollections.observableList(vendedores);
        this.cmbVendedor.setItems(this.datosCmbVendedor);
        this.datosCmbProducto = FXCollections.observableList(productos);
        this.cmbProducto.setItems(this.datosCmbProducto);
        this.progress.setVisible(false);
    }

    private void configureTable() {
        TableColumn<DetallePedido, String> tcProd = (TableColumn<DetallePedido, String>) this.tableView.getColumns().get(0);
        tcProd.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getProducto().toString());
        });
        TableColumn<DetallePedido, Float> tcCant = (TableColumn<DetallePedido, Float>) this.tableView.getColumns().get(1);
        tcCant.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        TableColumn<DetallePedido, Float> tcPrecVta = (TableColumn<DetallePedido, Float>) this.tableView.getColumns().get(2);
        tcPrecVta.setCellValueFactory(new PropertyValueFactory<>("precioVta"));
        TableColumn<DetallePedido, String> tcSubtotal = (TableColumn<DetallePedido, String>) this.tableView.getColumns().get(3);
        tcSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
    }

    //-----------------------------------
    //MENSAJE 17 DEL DIAGRAMA DE SECUENCIA
    //-----------------------------------
    public void passData(Pedido pedido, Controller otherCtrl) {
        this.otherCtrl = otherCtrl;
        this.txtNro.setText(Integer.toString(pedido.getNro()));
        this.txtFecha.setValue(new java.sql.Date(pedido.getFecha().getTime()).toLocalDate());
        this.cmbCliente.setValue(pedido.getCliente());
        this.cmbVendedor.setValue(pedido.getVendedor());
        this.pedido = pedido;
        this.pedido.buscarDetalles();
        this.datos = FXCollections.observableList(this.pedido.getDetalles());
        this.tableView.setItems(this.datos);
        this.btnGuardar.setDisable(false);
        if (this.datos.isEmpty()) {
            this.btnGuardar.setDisable(true);
        }
        float total = this.pedido.calcularTotalDetalle();
        this.txtTotal.setText(String.valueOf(total));
    }

    //-----------------------------------
    //MENSAJE 19 DEL DIAGRAMA DE SECUENCIA
    //-----------------------------------
    @FXML
    private void agregarItemDetalle() {
        if (validador.containsErrors() || this.cmbProducto.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.WARNING, null, "Info", "Para agregar un nuevo item debe seleccionar un Producto y los demás campos requeridos");
            return;
        }
        Producto prod = this.cmbProducto.getSelectionModel().getSelectedItem();
        if (prod != null) {
            String cantStr = this.txtCantidad.getText();
            String precStr = this.txtPrecio.getText();
            float cant = Float.parseFloat(cantStr);
            float prec = Float.parseFloat(precStr);
            if (this.pedido.agregarItemDetallePedido(prod, cant, prec)) {
                this.datos = FXCollections.observableList(this.pedido.getDetalles());
                this.tableView.setItems(this.datos);

                //-----------------------------------
                //MENSAJE 20 DEL DIAGRAMA DE SECUENCIA
                //-----------------------------------
                float total = this.pedido.calcularTotalDetalle();
                this.txtTotal.setText(String.valueOf(total));
            } else {
                showAlert(Alert.AlertType.WARNING, null, "Info", "Ese producto ya esta en el detalle");
            }
            this.txtCantidad.setText("");
            this.txtPrecio.setText("");
            this.cmbProducto.setValue(null);
            this.tableView.getSelectionModel().clearSelection();
        }
        this.btnGuardar.setDisable(false);
        if (this.tableView.getItems().isEmpty()) {
            this.btnGuardar.setDisable(true);
        }
    }

    @FXML
    private void quitarItemDetalle() {
        DetallePedido det = this.tableView.getSelectionModel().getSelectedItem();
        if (det != null) {
            this.tableView.getItems().remove(det);
            float total = this.pedido.calcularTotalDetalle();
            this.txtTotal.setText(String.valueOf(total));
            this.tableView.getSelectionModel().clearSelection();
        }
        this.txtCantidad.setText("");
        this.txtPrecio.setText("");
        this.cmbProducto.setValue(null);
        this.btnGuardar.setDisable(false);
        if (this.tableView.getItems().isEmpty()) {
            this.btnGuardar.setDisable(true);
        }
    }

    //-----------------------------------
    //MENSAJE 23 DEL DIAGRAMA DE SECUENCIA
    //-----------------------------------
    @FXML
    private void guardarPedido() {
        if (this.txtFecha.getValue() == null
                || this.cmbCliente.getSelectionModel().getSelectedItem() == null
                || this.cmbVendedor.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.WARNING, null, "Info", "Debe seleccionar una fecha, un cliente y un vendedor");
            return;
        }
        if (this.datos.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, null, "Info", "No puede guardar un pedido sin un detalle");
            return;
        }
        boolean resp;
        this.pedido.setCliente(this.cmbCliente.getSelectionModel().getSelectedItem());
        this.pedido.setVendedor(this.cmbVendedor.getSelectionModel().getSelectedItem());
        this.pedido.setDetalles(this.datos);
        this.pedido.setFecha(Date.from(this.txtFecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (this.pedido.getNro() > -1) {
            resp = this.pedido.modificar();
        } else {
            //-----------------------------------
            //MENSAJE 24 DEL DIAGRAMA DE SECUENCIA
            //-----------------------------------
            resp = this.pedido.guardar();
        }
        if (resp) {
            this.otherCtrl.loadData();
            showAlert(Alert.AlertType.INFORMATION, null, "Info", "Pedido guardado con exito");
            this.btnCerrarEditarPedido.fire();
        } else {
            showAlert(Alert.AlertType.ERROR, null, "Info", "El Pedido no pudo ser guardado");
        }
    }

    @FXML
    private void alCambiarProducto(ActionEvent event) {
        Producto prod = this.cmbProducto.getSelectionModel().getSelectedItem();
        this.txtCantidad.setText("");
        this.txtPrecio.setText("");
        if (prod != null) {
            this.txtCantidad.setText("1.0");
            this.txtPrecio.setText(String.valueOf(prod.getPrecio()));
        }
    }

    @FXML
    private void obtenerDetallePedidoSeleccionado(MouseEvent evt) {
        if (evt.getClickCount() == 1) {
            DetallePedido det = this.tableView.getSelectionModel().getSelectedItem();
            if (det != null) {
                this.cmbProducto.setValue(det.getProducto());
                this.txtCantidad.setText(String.valueOf(det.getCantidad()));
                this.txtPrecio.setText(String.valueOf(det.getPrecioVta()));
            }
        }
    }


}
