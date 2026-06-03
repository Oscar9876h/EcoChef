package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.example.ecochef.dao.RecetaDAOImpl;
import org.example.ecochef.model.Receta;
import org.example.ecochef.model.SesionActiva;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RecetasController implements Initializable {

    // Contenedores existentes
    @FXML private VBox flowRecetasContenedor;
    @FXML private Button btnAnadirReceta;

    // Elementos de la tabla que ya tienes en tu FXML
    @FXML private TableView<Receta> tablaRecetas;
    @FXML private TableColumn<Receta, Integer> colId;
    @FXML private TableColumn<Receta, String> colNombre;

    private final RecetaDAOImpl recetaDAO = new RecetaDAOImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Control de visibilidad
        if (!SesionActiva.esAdmin()) {
            btnAnadirReceta.setVisible(false);
            btnAnadirReceta.setManaged(false);
            tablaRecetas.setVisible(false);
            tablaRecetas.setManaged(false);
        }

        // Configuración de la tabla
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreRecetaProperty());

        cargarRecetas();
    }

    // Botón que carga los datos en la tabla (asociado en Scene Builder al botón)
    @FXML
    public void abrirFormularioAnadir() {
        tablaRecetas.getItems().setAll(recetaDAO.listarTodos());
    }

    // --- TU LÓGICA ORIGINAL SE MANTIENE ABAJO ---

    public void cargarRecetas() {
        List<Receta> lista = recetaDAO.listarTodos();
        flowRecetasContenedor.getChildren().clear();

        if (lista.isEmpty()) {
            flowRecetasContenedor.getChildren().add(new Label("No hay recetas disponibles."));
            return;
        }

        List<Receta> saludables = filtrarPorKeywords(lista, "GAZPACHO", "ENSALADA", "VERDURA", "CREMA");
        List<Receta> caprichos = filtrarPorKeywords(lista, "CARBONARA", "PASTA", "HAMBURGUESA", "PIZZA");
        List<Receta> otras = lista.stream().filter(r -> !saludables.contains(r) && !caprichos.contains(r)).collect(Collectors.toList());

        if (!saludables.isEmpty()) crearBloqueSeccion("RECETAS SALUDABLES", saludables);
        if (!caprichos.isEmpty()) crearBloqueSeccion("OPCIONES MENOS SALUDABLES", caprichos);
        if (!otras.isEmpty()) crearBloqueSeccion("OTRAS PROPUESTAS", otras);
    }

    private List<Receta> filtrarPorKeywords(List<Receta> lista, String... keywords) {
        return lista.stream().filter(r -> {
            String n = r.getNombreReceta().toUpperCase();
            for (String k : keywords) if (n.contains(k)) return true;
            return false;
        }).collect(Collectors.toList());
    }

    private void crearBloqueSeccion(String titulo, List<Receta> lista) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ecochef/seccion-titulo.fxml"));
            Label lblTitulo = loader.load();
            lblTitulo.setText(titulo + " (" + lista.size() + ")");
            flowRecetasContenedor.getChildren().add(lblTitulo);
        } catch (IOException e) {
            System.err.println("Error cargando título: " + e.getMessage());
        }

        FlowPane flowSeccion = new FlowPane();
        for (Receta receta : lista) {
            flowSeccion.getChildren().add(cargarTarjeta(receta));
        }
        flowRecetasContenedor.getChildren().add(flowSeccion);
    }

    private Node cargarTarjeta(Receta receta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ecochef/tarjeta-receta.fxml"));
            VBox tarjeta = loader.load();

            Label lbNombre = (Label) tarjeta.lookup("#lbNombre");
            Label lbTiempo = (Label) tarjeta.lookup("#lbTiempo");
            Label lbDesc = (Label) tarjeta.lookup("#lbDesc");
            Button btnCheck = (Button) tarjeta.lookup("#btnCheck");
            Button btnEliminar = (Button) tarjeta.lookup("#btnEliminar");

            if (lbNombre != null) lbNombre.setText(receta.getNombreReceta().toUpperCase());
            if (lbTiempo != null) lbTiempo.setText("⏱ " + receta.getTiempoPreparacion() + " min");
            if (lbDesc != null) lbDesc.setText(receta.getDescripcion());

            if (btnCheck != null) {
                btnCheck.setOnAction(event -> {
                    String disponibilidad = recetaDAO.comprobarDisponibilidad(receta.getId());
                    mostrarAlerta("Ingredientes para " + receta.getNombreReceta(), disponibilidad);
                });
            }

            if (btnEliminar != null) {
                btnEliminar.setOnAction(event -> {
                    recetaDAO.eliminar(receta.getId());
                    cargarRecetas();
                });
                if (!SesionActiva.esAdmin()) {
                    btnEliminar.setVisible(false);
                    btnEliminar.setManaged(false);
                }
            }
            return tarjeta;
        } catch (IOException e) {
            return new Label("Error al cargar tarjeta");
        }
    }

    private void mostrarAlerta(String cabecera, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("EcoChef - Verificación");
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}