/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import org.ubp.edu.ar.ejemplocompletofx.App;

/**
 * FXML Controller class
 *
 * @author agustin
 */
public class PrincipalController extends Controller implements Initializable {

    private Alert alert;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.alert = new Alert(Alert.AlertType.CONFIRMATION);
        this.alert.setTitle("Info");
        this.alert.setHeaderText("¿Seguro?");
        this.alert.setContentText("Desea realizar esa acción");
    }

    @FXML
    private void salir() {
        this.alert.showAndWait()
                .ifPresent((btnType) -> {
                    if (btnType == ButtonType.OK) {
                        Platform.exit();
                    }
                });
    }

    //----------------------------
    //MENSAJE 2 DIAGRAMA SECUENCIA
    //----------------------------
    @FXML
    private void opcConsultarPedido() {
        try {
            App.openFXML("pedidos", "Consulta de pedidos", Modality.APPLICATION_MODAL);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, null, "Info", ex.toString());
        }
    }

    @FXML
    private void opcConsultarProductos() {
        try {
            App.openFXMLResource("/fxml/ProductosVista.fxml", "Gestión de productos", Modality.APPLICATION_MODAL);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, null, "Productos", ex.getMessage());
        }
    }

    @FXML
    private void opcConsultarClientes() {
        try {
            App.openFXMLResource("/fxml/ClientesVista.fxml", "Gestión de clientes", Modality.APPLICATION_MODAL);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, null, "Clientes", ex.getMessage());
        }
    }

    @FXML
    private void opcAcercaDe() {
        showAlert(Alert.AlertType.INFORMATION, null, "Info", "Otra alucinada de agusrafafer!");
    }

}
