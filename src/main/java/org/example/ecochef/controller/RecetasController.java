package org.example.ecochef.controller;

import javafx.application.Platform;
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

/**
 * Controlador de la vista de Recetas.
 * Se encarga de mostrar las recetas clasificadas por categorías (Saludables, Caprichos, etc.)
 * y gestionar la interacción con el usuario dependiendo de sus privilegios.
 */
public class RecetasController implements Initializable {

    @FXML private VBox flowRecetasContenedor; // Contenedor vertical de todas las secciones
    @FXML private Button btnAnadirReceta;    // Botón visible solo para administradores

    private final RecetaDAOImpl recetaDAO = new RecetaDAOImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Control de acceso: Ocultar botones de gestión si el usuario no es admin
        if (!SesionActiva.esAdmin()) {
            btnAnadirReceta.setVisible(false);
            btnAnadirReceta.setManaged(false);
        }
        cargarRecetas();
    }

    /**
     * Recupera todas las recetas de la BD, las clasifica mediante filtrado
     * y las añade a la vista organizada por bloques.
     */
    public void cargarRecetas() {
        List<Receta> lista = recetaDAO.listarTodos();
        flowRecetasContenedor.getChildren().clear();

        if (lista.isEmpty()) {
            flowRecetasContenedor.getChildren().add(new Label("No hay recetas disponibles."));
            return;
        }

        // Clasificación lógica de recetas basada en palabras clave
        List<Receta> saludables = filtrarPorKeywords(lista, "GAZPACHO", "ENSALADA", "VERDURA", "CREMA");
        List<Receta> caprichos = filtrarPorKeywords(lista, "CARBONARA", "PASTA", "HAMBURGUESA", "PIZZA");
        List<Receta> otras = lista.stream().filter(r -> !saludables.contains(r) && !caprichos.contains(r)).collect(Collectors.toList());

        // Generar secciones en la UI solo si contienen elementos
        if (!saludables.isEmpty()) crearBloqueSeccion("RECETAS SALUDABLES", saludables);
        if (!caprichos.isEmpty()) crearBloqueSeccion("OPCIONES MENOS SALUDABLES", caprichos);
        if (!otras.isEmpty()) crearBloqueSeccion("OTRAS PROPUESTAS", otras);
    }

    /**
     * Filtra una lista de recetas basándose en si su nombre contiene alguna de las palabras clave.
     */
    private List<Receta> filtrarPorKeywords(List<Receta> lista, String... keywords) {
        return lista.stream().filter(r -> {
            String n = r.getNombreReceta().toUpperCase();
            for (String k : keywords) if (n.contains(k)) return true;
            return false;
        }).collect(Collectors.toList());
    }

    /**
     * Crea un título de sección y un contenedor para las tarjetas correspondientes.
     */
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

    /**
     * Carga y personaliza la tarjeta visual (FXML) para una receta específica.
     */
    private Node cargarTarjeta(Receta receta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ecochef/tarjeta-receta.fxml"));
            VBox tarjeta = loader.load();

            // Referencia a los componentes del FXML para asignar datos
            Label lbNombre = (Label) tarjeta.lookup("#lbNombre");
            Label lbTiempo = (Label) tarjeta.lookup("#lbTiempo");
            Label lbDesc = (Label) tarjeta.lookup("#lbDesc");
            Button btnCheck = (Button) tarjeta.lookup("#btnCheck");
            Button btnEliminar = (Button) tarjeta.lookup("#btnEliminar");

            if (lbNombre != null) lbNombre.setText(receta.getNombreReceta().toUpperCase());
            if (lbTiempo != null) lbTiempo.setText("⏱ " + receta.getTiempoPreparacion() + " min");
            if (lbDesc != null) lbDesc.setText(receta.getDescripcion());

            // Acción: Verificar si los ingredientes están disponibles en la despensa
            if (btnCheck != null) {
                btnCheck.setOnAction(event -> {
                    String disponibilidad = recetaDAO.comprobarDisponibilidad(receta.getId());
                    mostrarAlerta("Ingredientes para " + receta.getNombreReceta(), disponibilidad);
                });
            }

            // Acción: Eliminar receta (Solo visible para admins)
            if (btnEliminar != null) {
                btnEliminar.setOnAction(event -> {
                    recetaDAO.eliminar(receta.getId());
                    cargarRecetas(); // Refrescar vista
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

    @FXML
    public void abrirFormularioAnadir() {
        System.out.println("Formulario abierto");
    }
}