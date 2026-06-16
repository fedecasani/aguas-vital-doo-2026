package org.ubp.edu.ar.ejemplocompletofx.controladores;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ubp.edu.ar.ejemplocompletofx.dao.BarrioDao;
import org.ubp.edu.ar.ejemplocompletofx.dao.ClienteDao;
import org.ubp.edu.ar.ejemplocompletofx.dto.BarrioDto;
import org.ubp.edu.ar.ejemplocompletofx.dto.ClienteDto;
import org.ubp.edu.ar.ejemplocompletofx.factories.FabricaModelo;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Cliente;

public class ClientesController extends Controller implements Initializable {

    private Cliente clienteModelo;
    private ClienteDto clienteSeleccionado;
    private final BarrioDao barrioDao = new BarrioDao();

    @FXML
    private TableView<ClienteDto> tablaClientes;

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtDni;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private ComboBox<BarrioDto> cmbBarrio;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnBuscar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteModelo = (Cliente) FabricaModelo.fabricar("Cliente");
        cargarBarrios();
        configurarTabla();
        loadData();
        tablaClientes.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, seleccionado) -> cargarFormulario(seleccionado));
    }

    @Override
    public void loadData() {
        ClienteDao dao = (ClienteDao) clienteModelo.dao;
        tablaClientes.setItems(FXCollections.observableArrayList(dao.listarTodos()));
    }

    private void cargarBarrios() {
        List<BarrioDto> barrios = barrioDao.listarTodos();
        cmbBarrio.setItems(FXCollections.observableArrayList(barrios));
        cmbBarrio.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(BarrioDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatearBarrio(item));
            }
        });
        cmbBarrio.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(BarrioDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatearBarrio(item));
            }
        });
    }

    private String formatearBarrio(BarrioDto barrio) {
        return barrio.getNombre() + " (" + barrio.getZona().getNombre() + ")";
    }

    private void configurarTabla() {
        TableColumn<ClienteDto, String> colApellido
                = (TableColumn<ClienteDto, String>) tablaClientes.getColumns().get(0);
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        TableColumn<ClienteDto, String> colNombre
                = (TableColumn<ClienteDto, String>) tablaClientes.getColumns().get(1);
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<ClienteDto, String> colDni
                = (TableColumn<ClienteDto, String>) tablaClientes.getColumns().get(2);
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));

        TableColumn<ClienteDto, String> colTelefono
                = (TableColumn<ClienteDto, String>) tablaClientes.getColumns().get(3);
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        TableColumn<ClienteDto, String> colDireccion
                = (TableColumn<ClienteDto, String>) tablaClientes.getColumns().get(4);
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        TableColumn<ClienteDto, String> colBarrio
                = (TableColumn<ClienteDto, String>) tablaClientes.getColumns().get(5);
        colBarrio.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getBarrio() == null ? ""
                        : formatearBarrio(data.getValue().getBarrio())));
    }

    private void cargarFormulario(ClienteDto cliente) {
        clienteSeleccionado = cliente;
        if (cliente == null) {
            return;
        }
        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        txtDni.setText(cliente.getDni());
        txtTelefono.setText(cliente.getTelefono());
        txtDireccion.setText(cliente.getDireccion());
        if (cliente.getBarrio() != null) {
            cmbBarrio.getItems().stream()
                    .filter(b -> b.getId() == cliente.getBarrio().getId())
                    .findFirst()
                    .ifPresent(b -> cmbBarrio.setValue(b));
        }
    }

    @FXML
    private void accionBuscar(ActionEvent event) {
        String criterio = txtBuscar.getText().trim();
        ClienteDao dao = (ClienteDao) clienteModelo.dao;
        if (criterio.isEmpty()) {
            loadData();
            return;
        }
        ClienteDto filtro = new ClienteDto();
        filtro.setDni(criterio);
        tablaClientes.setItems(FXCollections.observableArrayList(dao.listarPorCriterio(filtro)));
    }

    @FXML
    private void accionGuardar(ActionEvent event) {
        try {
            ClienteDto dto = construirDtoDesdeFormulario();
            ClienteDao dao = (ClienteDao) clienteModelo.dao;
            boolean esAlta = clienteSeleccionado == null;
            boolean guardado = esAlta ? dao.insertar(dto) : dao.modificar(dto);
            if (guardado) {
                loadData();
                accionLimpiar(event);
                showAlert(Alert.AlertType.INFORMATION, null, "Clientes",
                        esAlta ? "Cliente registrado correctamente."
                                : "Cliente actualizado correctamente.");
            }
        } catch (RuntimeException ex) {
            showAlert(Alert.AlertType.WARNING, null, "Clientes", ex.getMessage());
        }
    }

    @FXML
    private void accionEliminar(ActionEvent event) {
        ClienteDto seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            showAlert(Alert.AlertType.INFORMATION, null, "Clientes",
                    "Seleccione un cliente para eliminar.");
            return;
        }
        try {
            ClienteDao dao = (ClienteDao) clienteModelo.dao;
            if (dao.borrar(seleccionado)) {
                loadData();
                accionLimpiar(event);
                showAlert(Alert.AlertType.INFORMATION, null, "Clientes",
                        "Cliente eliminado correctamente.");
            }
        } catch (RuntimeException ex) {
            showAlert(Alert.AlertType.WARNING, null, "Clientes", ex.getMessage());
        }
    }

    @FXML
    private void accionLimpiar(ActionEvent event) {
        clienteSeleccionado = null;
        tablaClientes.getSelectionModel().clearSelection();
        txtNombre.clear();
        txtApellido.clear();
        txtDni.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        cmbBarrio.getSelectionModel().clearSelection();
    }

    private ClienteDto construirDtoDesdeFormulario() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String dni = txtDni.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        BarrioDto barrio = cmbBarrio.getValue();
        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()
                || telefono.isEmpty() || direccion.isEmpty() || barrio == null) {
            throw new IllegalArgumentException("Complete todos los campos y seleccione un barrio.");
        }
        ClienteDto dto = new ClienteDto();
        if (clienteSeleccionado != null) {
            dto.setId(clienteSeleccionado.getId());
            dto.setTipoDocumento(clienteSeleccionado.getTipoDocumento());
            dto.setRazonSocial(clienteSeleccionado.getRazonSocial());
        } else {
            dto.setTipoDocumento(dni.length() > 8 ? "CUIT" : "DNI");
            dto.setRazonSocial(null);
        }
        dto.setNombre(nombre);
        dto.setApellido(apellido);
        dto.setDni(dni);
        dto.setTelefono(telefono);
        dto.setDireccion(direccion);
        dto.setBarrio(barrio);
        dto.setActivo(true);
        return dto;
    }
}
