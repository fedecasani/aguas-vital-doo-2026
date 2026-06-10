package org.ubp.edu.ar.ejemplocompletofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("principal"), 640, 480);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Sistema de gestión de pedidos");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Info");
        alert.setHeaderText("¿Seguro?");
        alert.setContentText("Desea realizar esa acción");
        stage.setOnCloseRequest(e -> {
            alert.showAndWait()
                    .ifPresent((btnType) -> {
                        if (btnType == ButtonType.OK) {
                            Platform.exit();
                        } else {
                            e.consume();
                        }
                    });
        });
        stage.show();
    }

    static public void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /***
     * Con este método podemos abrir cualquir FXML y obtener el loader
     * para así obtener luego el controlador asociado
     * @param fxml el nombre de la vista sin la extención fxml
     * @param title el titulo de lavista
     * @param modality como queremos abrir la vista 
     * @return devuelve un loader con la vista y su controlador asociado
     * @throws IOException si no se encuentra el nombre del archivo dispara una excepción de IO
     */
    public static FXMLLoader openFXML(String fxml, String title, Modality modality) throws IOException {
        Stage newWindow = new Stage();
        newWindow.setTitle(title);
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        newWindow.setScene(new Scene(loader.load()));
        newWindow.setResizable(false);
//        newWindow.initStyle(StageStyle.UTILITY);
//        newWindow.toFront();
//        newWindow.initOwner(loader.getRoot());
        newWindow.initModality(modality);
        newWindow.show();
        return loader;
    }

    public static void main(String[] args) {
        launch();
    }

}
