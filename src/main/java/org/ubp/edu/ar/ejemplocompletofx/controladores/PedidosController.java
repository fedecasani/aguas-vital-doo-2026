/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.controladores;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import net.sf.jasperreports.engine.JRException;
import org.ubp.edu.ar.ejemplocompletofx.App;
import org.ubp.edu.ar.ejemplocompletofx.factories.FabricaModelo;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Modelo;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Pedido;

/**
 * FXML Controller class
 *
 * @author agustin
 */
public class PedidosController extends Controller implements Initializable {

    private Modelo modelo;
    @FXML
    private TableView<Pedido> tableView;
    private ObservableList<Pedido> datos = null;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnReporte;

    //-----------------------------------
    //MENSAJE 4 DEL DIAGRAMA DE SECUENCIA
    //-----------------------------------
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.modelo = FabricaModelo.fabricar("Pedido");
        this.txtBuscar.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtBuscar.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        if (this.tableView != null) {
            this.configureTable();
            this.loadData();
        }

    }

    //-----------------------------------
    //MENSAJE 5 DEL DIAGRAMA DE SECUENCIA
    //-----------------------------------
    @Override
    public void loadData() {
        this.progress.setVisible(true);
        List<Pedido> pedidos = ((Pedido) this.modelo).listarTodos();
        this.datos = FXCollections.observableList(pedidos);
        this.tableView.setItems(datos);
        this.tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.progress.setVisible(false);
    }

    private void configureTable() {
        TableColumn<Pedido, Integer> tcNro = (TableColumn<Pedido, Integer>) this.tableView.getColumns().get(0);
        tcNro.setCellValueFactory(new PropertyValueFactory<>("nro"));
        TableColumn<Pedido, String> tcFecha = (TableColumn<Pedido, String>) this.tableView.getColumns().get(1);
        tcFecha.setCellValueFactory(celda -> {
            Date fecha = celda.getValue().getFecha();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            return new SimpleStringProperty(fecha != null ? sdf.format(fecha) : "");
        });
        TableColumn<Pedido, String> tcCliente = (TableColumn<Pedido, String>) this.tableView.getColumns().get(2);
        tcCliente.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getCliente() != null ? cellData.getValue().getCliente().toString() : "");
        });
    }

    @FXML
    private void obtenerPedidoSeleccionado(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            this.btnModificar.fire();
        }
    }

    @FXML
    private void buscarPedidos() {
        this.progress.setVisible(true);
        String texto = this.txtBuscar.getText();
        List<Pedido> pedidos;
        if (!texto.isEmpty()) {
            Integer nro = Integer.valueOf(texto);
            pedidos = ((Pedido) this.modelo).listarPorNro(nro);
        } else {
            pedidos = ((Pedido) this.modelo).listarTodos();
        }
        this.datos = FXCollections.observableList(pedidos);
        this.tableView.setItems(this.datos);
        this.progress.setVisible(false);
    }

    @FXML
    private void modificarPedido(ActionEvent event) {
        Pedido ped = this.tableView.getSelectionModel().getSelectedItem();
        if (ped != null) {
            try {
                FXMLLoader loader = App.openFXML("editarPedido", "Editar pedido", Modality.APPLICATION_MODAL);
                EditarPedidoController controller = loader.getController();
                controller.passData(ped, this);
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, null, "Error", ex.toString());
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, null, "Info", "Seleccione un pedido para moficar");
        }
    }

    //-----------------------------------
    //MENSAJE 7 DEL DIAGRAMA DE SECUENCIA
    //-----------------------------------
    @FXML
    private void nuevoPedido(ActionEvent event) {
        try {
            FXMLLoader loader = App.openFXML("editarPedido", "Nuevo pedido", Modality.APPLICATION_MODAL);
            EditarPedidoController controller = loader.getController();

            //-----------------------------------
            //MENSAJE 17 DEL DIAGRAMA DE SECUENCIA
            //-----------------------------------
            controller.passData(new Pedido(-1, new Date(System.currentTimeMillis()), null, null), this);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, null, "Error", ex.toString());
        }
    }

    @FXML
    private void eliminarPedido(ActionEvent event) {
        Pedido ped = this.tableView.getSelectionModel().getSelectedItem();
        if (ped == null) {
            showAlert(Alert.AlertType.INFORMATION, null, "Info", "Seleccione un pedido para eliminar");
            return;
        }
        Alert confirma = new Alert(Alert.AlertType.CONFIRMATION);
        confirma.setTitle("Info");
        confirma.setHeaderText("¿Seguro desea eliminar ese registro?");
        confirma.showAndWait()
                .ifPresent((btnType) -> {
                    if (btnType == ButtonType.OK) {
                        this.modelo = this.tableView.getSelectionModel().getSelectedItem();
                        if (((Pedido) this.modelo).eliminar()) {
                            showAlert(Alert.AlertType.INFORMATION, null, "Info", "Registro eliminado con exito");
                            this.loadData();
                        } else {
                            showAlert(Alert.AlertType.ERROR, null, "Info", "El registro no pudo ser eliminado");
                        }
                    }
                });
    }

    @FXML
    private void limpiarBusqueda(ActionEvent event) {
        this.txtBuscar.setText("");
        this.loadData();
    }

    @FXML
    private void generarReporte() {
        try {
            ((Pedido) this.modelo).generarReporte();
        } catch (FileNotFoundException | JRException ex) {
            showAlert(Alert.AlertType.ERROR, null, "Info", ex.toString());
        }
    }

}
