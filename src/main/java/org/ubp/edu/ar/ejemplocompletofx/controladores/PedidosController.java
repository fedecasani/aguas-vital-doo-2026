package org.ubp.edu.ar.ejemplocompletofx.controladores;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import org.ubp.edu.ar.ejemplocompletofx.App;
import org.ubp.edu.ar.ejemplocompletofx.factories.FabricaModelo;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Pedido;

public class PedidosController extends Controller implements Initializable {

    private Pedido pedidoModelo;
    @FXML
    private TableView<Pedido> tableView;
    @FXML
    private TextField txtBuscar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pedidoModelo = (Pedido) FabricaModelo.fabricar("Pedido");
        configurarTabla();
        loadData();
    }

    @Override
    public void loadData() {
        progress.setVisible(true);
        tableView.setItems(FXCollections.observableArrayList(pedidoModelo.listarTodos()));
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        progress.setVisible(false);
    }

    private void configurarTabla() {
        TableColumn<Pedido, Integer> nro = (TableColumn<Pedido, Integer>) tableView.getColumns().get(0);
        nro.setCellValueFactory(new PropertyValueFactory<>("nro"));
        configurarFecha(1, Pedido::getFecha);
        configurarFecha(2, Pedido::getFechaHoraEstimada);
        TableColumn<Pedido, String> cliente = (TableColumn<Pedido, String>) tableView.getColumns().get(3);
        cliente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCliente().toString()));
        TableColumn<Pedido, String> distribuidor = (TableColumn<Pedido, String>) tableView.getColumns().get(4);
        distribuidor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDistribuidor().toString()));
        TableColumn<Pedido, String> estado = (TableColumn<Pedido, String>) tableView.getColumns().get(5);
        estado.setCellValueFactory(new PropertyValueFactory<>("nombreEstado"));
    }

    private void configurarFecha(int indice, java.util.function.Function<Pedido, Date> getter) {
        TableColumn<Pedido, String> columna = (TableColumn<Pedido, String>) tableView.getColumns().get(indice);
        columna.setCellValueFactory(data -> {
            Date fecha = getter.apply(data.getValue());
            return new SimpleStringProperty(fecha == null ? ""
                    : new SimpleDateFormat("dd/MM/yyyy HH:mm").format(fecha));
        });
    }

    @FXML
    private void buscarPedidos() {
        String texto = txtBuscar.getText().trim();
        List<Pedido> pedidos = texto.isEmpty()
                ? pedidoModelo.listarTodos()
                : pedidoModelo.listarPorNro(Integer.parseInt(texto));
        tableView.setItems(FXCollections.observableArrayList(pedidos));
    }

    @FXML
    private void limpiarBusqueda() {
        txtBuscar.clear();
        loadData();
    }

    @FXML
    private void nuevoPedido() {
        try {
            FXMLLoader loader = App.openFXML("editarPedido", "Registrar pedido", Modality.APPLICATION_MODAL);
            EditarPedidoController controller = loader.getController();
            controller.passData(this);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, null, "Registrar pedido", ex.getMessage());
        }
    }

    @FXML
    private void actualizarPedido() {
        Pedido pedido = tableView.getSelectionModel().getSelectedItem();
        if (pedido == null) {
            showAlert(Alert.AlertType.INFORMATION, null, "Actualizar pedido",
                    "Seleccione un pedido.");
            return;
        }
        if (!"PENDIENTE".equals(pedido.getNombreEstado())) {
            showAlert(Alert.AlertType.INFORMATION, null, "Actualizar pedido",
                    "Solo se pueden actualizar pedidos pendientes.");
            return;
        }
        try {
            FXMLLoader loader = App.openFXML("actualizarPedido", "Actualizar pedido", Modality.APPLICATION_MODAL);
            ActualizarPedidoController controller = loader.getController();
            controller.passData(pedido, this);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, null, "Actualizar pedido", ex.getMessage());
        }
    }

    @FXML
    private void obtenerPedidoSeleccionado(MouseEvent event) {
        if (event.getClickCount() == 2) {
            actualizarPedido();
        }
    }
}
