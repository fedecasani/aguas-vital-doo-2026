package org.ubp.edu.ar.ejemplocompletofx.controladores;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import org.ubp.edu.ar.ejemplocompletofx.modelo.FormaPago;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Pedido;

public class ActualizarPedidoController extends Controller implements Initializable {

    private Pedido pedido;
    private Controller parentController;
    @FXML
    private Label lblPedido;
    @FXML
    private Label lblCliente;
    @FXML
    private Label lblTotal;
    @FXML
    private RadioButton rbEntregado;
    @FXML
    private RadioButton rbPendiente;
    @FXML
    private DatePicker dpFechaEntrega;
    @FXML
    private ComboBox<FormaPago> cmbFormaPago;
    @FXML
    private TextArea txtMotivo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup grupo = new ToggleGroup();
        rbEntregado.setToggleGroup(grupo);
        rbPendiente.setToggleGroup(grupo);
        rbEntregado.setSelected(true);
        dpFechaEntrega.setValue(java.time.LocalDate.now());
        cmbFormaPago.setItems(FXCollections.observableArrayList(FormaPago.values()));
        cmbFormaPago.setValue(FormaPago.EFECTIVO);
        actualizarCampos();
    }

    public void passData(Pedido pedido, Controller parentController) {
        this.pedido = pedido;
        this.parentController = parentController;
        pedido.buscarDetalles();
        lblPedido.setText("Pedido Nro. " + pedido.getNro());
        lblCliente.setText(pedido.getCliente().toString());
        lblTotal.setText(String.format("$ %.2f", pedido.calcularTotalDetalle()));
    }

    @FXML
    private void actualizarCampos() {
        boolean entregado = rbEntregado.isSelected();
        dpFechaEntrega.setDisable(!entregado);
        cmbFormaPago.setDisable(!entregado);
        txtMotivo.setDisable(entregado);
    }

    @FXML
    private void guardarActualizacion() {
        try {
            boolean actualizado;
            if (rbEntregado.isSelected()) {
                Date fechaEntrega = Date.from(dpFechaEntrega.getValue()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());
                actualizado = pedido.registrarEntrega(fechaEntrega, cmbFormaPago.getValue());
            } else {
                actualizado = pedido.mantenerPendienteYGuardar(txtMotivo.getText());
            }
            if (actualizado) {
                parentController.loadData();
                showAlert(Alert.AlertType.INFORMATION, null, "Actualizar pedido",
                        "Pedido actualizado correctamente.");
                lblPedido.getScene().getWindow().hide();
            }
        } catch (RuntimeException ex) {
            showAlert(Alert.AlertType.WARNING, null, "Actualizar pedido", ex.getMessage());
        }
    }
}
