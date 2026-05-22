package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.ecochef.dao.AlimentoDAOImpl;
import org.example.ecochef.model.Alimento;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controlador encargado de gestionar la vista de la Despensa.
 * Maneja la carga de alimentos, su organización por categorías y la eliminación de los mismos.
 */
public class DespensaController implements Initializable {

    // Contenedor principal donde se inyectan dinámicamente las tarjetas de alimentos
    @FXML private VBox flowAlimentos;

    // Objeto de acceso a datos para realizar operaciones CRUD en la base de datos
    private final AlimentoDAOImpl alimentoDAO = new AlimentoDAOImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Al iniciar, cargamos la despensa inmediatamente
        cargarDespensa();
    }

    /**
     * Obtiene los alimentos de la BD, los agrupa por tipo y actualiza la interfaz.
     */
    public void cargarDespensa() {
        if (flowAlimentos == null) return;

        List<Alimento> lista = alimentoDAO.listarTodos();
        flowAlimentos.getChildren().clear(); // Limpiamos la vista actual

        if (lista.isEmpty()) {
            flowAlimentos.getChildren().add(new Label("Despensa vacía."));
            return;
        }

        // Agrupamos los alimentos por su campo 'tipo' usando Streams de Java
        Map<String, List<Alimento>> alimentosPorSeccion = lista.stream()
                .collect(Collectors.groupingBy(a -> a.getTipo() != null ? a.getTipo().toUpperCase() : "OTROS"));

        // Creamos una sección visual para cada tipo de alimento encontrado
        alimentosPorSeccion.forEach((seccion, listaAlimentos) -> {
            try {
                FXMLLoader loaderTitulo = new FXMLLoader(getClass().getResource("/org/example/ecochef/seccion-titulo.fxml"));
                Label txtSeccion = loaderTitulo.load();
                txtSeccion.setText("• " + seccion + " (" + listaAlimentos.size() + ")");
                flowAlimentos.getChildren().add(txtSeccion);
            } catch (IOException e) {
                System.err.println("Error título: " + e.getMessage());
            }

            FlowPane flowSeccion = new FlowPane();
            flowSeccion.setHgap(10);
            flowSeccion.setVgap(10);
            for (Alimento alimento : listaAlimentos) {
                flowSeccion.getChildren().add(cargarTarjeta(alimento));
            }
            flowAlimentos.getChildren().add(flowSeccion);
        });
    }

    /**
     * Carga el archivo FXML de la tarjeta individual y le asigna los datos del alimento.
     * @param alimento El alimento a mostrar en la tarjeta.
     * @return El componente visual (Node) de la tarjeta configurada.
     */
    private Node cargarTarjeta(Alimento alimento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ecochef/tarjeta-alimento.fxml"));
            VBox tarjeta = loader.load();

            // Buscamos los elementos internos de la tarjeta por su ID definido en el FXML
            Label lbNombre = (Label) tarjeta.lookup("#lbNombre");
            Label lbInfo = (Label) tarjeta.lookup("#lbInfo");
            Button btnEliminar = (Button) tarjeta.lookup("#btnEliminar");

            // Inyectamos los datos del modelo en la vista
            if (lbNombre != null) lbNombre.setText(alimento.getNombre().toUpperCase());
            if (lbInfo != null) lbInfo.setText(alimento.getTipo() + " | " + alimento.getCalorias() + " kcal");

            // Configuración del comportamiento del botón borrar
            if (btnEliminar != null) {
                btnEliminar.setOnAction(event -> {
                    alimentoDAO.eliminar(alimento.getId()); // Eliminamos de la base de datos
                    cargarDespensa(); // Recargamos la interfaz para ver el cambio
                });
            }
            return tarjeta;
        } catch (IOException e) {
            e.printStackTrace();
            return new Label("Error al cargar tarjeta");
        }
    }

    /**
     * Abre una ventana modal para introducir un nuevo alimento.
     */
    @FXML
    public void abrirFormularioAnadir() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ecochef/formulario-alimento.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Añadir nuevo alimento");
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal hasta cerrar esta
            stage.setScene(new Scene(root));
            stage.showAndWait();

            cargarDespensa(); // Al cerrar el formulario, refrescamos la despensa
        } catch (IOException e) {
            System.err.println("Error al abrir formulario: " + e.getMessage());
        }
    }
}