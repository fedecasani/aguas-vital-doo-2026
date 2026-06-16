package org.ubp.edu.ar.ejemplocompletofx.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ubp.edu.ar.ejemplocompletofx.dao.ProductoDao;
import org.ubp.edu.ar.ejemplocompletofx.dto.ProductoDto;
import org.ubp.edu.ar.ejemplocompletofx.factories.FabricaModelo;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Producto;
import org.ubp.edu.ar.ejemplocompletofx.modelo.TipoProducto;

public class ProductosController extends Controller implements Initializable {

    private Producto productoModelo;
    private ProductoDto productoSeleccionado;

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCodigoBarra;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtStock;
    @FXML
    private ComboBox<String> cmbTipoProducto;

    @FXML
    private TableView<ProductoDto> tablaProductos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productoModelo = (Producto) FabricaModelo.fabricar("Producto");
        cmbTipoProducto.setItems(FXCollections.observableArrayList(
                TipoProducto.AGUA_MINERAL.name(),
                TipoProducto.SODA.name()));
        configurarTabla();
        loadData();
        tablaProductos.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, seleccionado) -> cargarFormulario(seleccionado));
    }

    @Override
    public void loadData() {
        ProductoDao dao = (ProductoDao) productoModelo.dao;
        tablaProductos.setItems(FXCollections.observableArrayList(dao.listarTodos()));
    }

    private void configurarTabla() {
        TableColumn<ProductoDto, String> colNombre
                = (TableColumn<ProductoDto, String>) tablaProductos.getColumns().get(0);
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<ProductoDto, String> colCodigo
                = (TableColumn<ProductoDto, String>) tablaProductos.getColumns().get(1);
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codBarra"));

        TableColumn<ProductoDto, Float> colPrecio
                = (TableColumn<ProductoDto, Float>) tablaProductos.getColumns().get(2);
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        TableColumn<ProductoDto, Integer> colCapacidad
                = (TableColumn<ProductoDto, Integer>) tablaProductos.getColumns().get(3);
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidadLitros"));

        TableColumn<ProductoDto, String> colTipo
                = (TableColumn<ProductoDto, String>) tablaProductos.getColumns().get(4);
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(formatearTipo(data.getValue().getTipo())));
    }

    private String formatearTipo(String tipo) {
        if (tipo == null) {
            return "";
        }
        return TipoProducto.AGUA_MINERAL.name().equals(tipo) ? "Agua mineral" : "Soda";
    }

    private void cargarFormulario(ProductoDto producto) {
        productoSeleccionado = producto;
        if (producto == null) {
            return;
        }
        txtNombre.setText(producto.getNombre());
        txtCodigoBarra.setText(producto.getCodBarra());
        txtPrecio.setText(String.valueOf(producto.getPrecio()));
        txtStock.setText(String.valueOf(producto.getCapacidadLitros()));
        cmbTipoProducto.setValue(producto.getTipo());
    }

    @FXML
    private void accionGuardar(ActionEvent event) {
        try {
            ProductoDto dto = construirDtoDesdeFormulario();
            ProductoDao dao = (ProductoDao) productoModelo.dao;
            boolean esAlta = productoSeleccionado == null;
            boolean guardado = esAlta ? dao.insertar(dto) : dao.modificar(dto);
            if (guardado) {
                loadData();
                accionLimpiar(event);
                showAlert(Alert.AlertType.INFORMATION, null, "Productos",
                        esAlta ? "Producto registrado correctamente."
                                : "Producto actualizado correctamente.");
            }
        } catch (RuntimeException ex) {
            showAlert(Alert.AlertType.WARNING, null, "Productos", ex.getMessage());
        }
    }

    @FXML
    private void accionEliminar(ActionEvent event) {
        ProductoDto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            showAlert(Alert.AlertType.INFORMATION, null, "Productos",
                    "Seleccione un producto para eliminar.");
            return;
        }
        try {
            ProductoDao dao = (ProductoDao) productoModelo.dao;
            if (dao.borrar(seleccionado)) {
                loadData();
                accionLimpiar(event);
                showAlert(Alert.AlertType.INFORMATION, null, "Productos",
                        "Producto eliminado correctamente.");
            }
        } catch (RuntimeException ex) {
            showAlert(Alert.AlertType.WARNING, null, "Productos", ex.getMessage());
        }
    }

    @FXML
    private void accionLimpiar(ActionEvent event) {
        productoSeleccionado = null;
        tablaProductos.getSelectionModel().clearSelection();
        txtNombre.clear();
        txtCodigoBarra.clear();
        txtPrecio.clear();
        txtStock.clear();
        cmbTipoProducto.getSelectionModel().clearSelection();
    }

    private ProductoDto construirDtoDesdeFormulario() {
        String nombre = txtNombre.getText().trim();
        String codigo = txtCodigoBarra.getText().trim();
        String tipo = cmbTipoProducto.getValue();
        if (nombre.isEmpty() || codigo.isEmpty() || tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("Complete nombre, codigo de barra y rubro.");
        }
        float precio = Float.parseFloat(txtPrecio.getText().trim());
        int capacidad = Integer.parseInt(txtStock.getText().trim());
        if (precio < 0 || capacidad <= 0) {
            throw new IllegalArgumentException("Precio y capacidad deben ser valores validos.");
        }
        ProductoDto dto = new ProductoDto();
        if (productoSeleccionado != null) {
            dto.setId(productoSeleccionado.getId());
        }
        dto.setNombre(nombre);
        dto.setCodBarra(codigo);
        dto.setPrecio(precio);
        dto.setCapacidadLitros(capacidad);
        dto.setTipo(tipo);
        dto.setActivo(true);
        return dto;
    }
}
