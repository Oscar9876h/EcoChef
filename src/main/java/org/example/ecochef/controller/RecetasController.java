package org.example.ecochef.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.example.ecochef.dao.RecetaDAOImpl;
import org.example.ecochef.model.Receta;
import org.example.ecochef.model.SesionActiva;
import org.example.ecochef.utils.DatabaseConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RecetasController implements Initializable {

    @FXML private VBox flowRecetasContenedor;
    @FXML private VBox contenedorTarjetas;
    @FXML private Button btnAnadirReceta;
    @FXML private TableView<Receta> tablaRecetas;
    @FXML private TableColumn<Receta, Integer> colId;
    @FXML private TableColumn<Receta, String> colNombre;

    @FXML private TabPane tabPaneGeneral;
    @FXML private StackPane panelAdminStack;

    private final RecetaDAOImpl recetaDAO = new RecetaDAOImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boolean esAdmin = SesionActiva.esAdmin();

        btnAnadirReceta.setVisible(esAdmin);
        btnAnadirReceta.setManaged(esAdmin);
        tablaRecetas.setVisible(esAdmin);
        tablaRecetas.setManaged(esAdmin);

        tablaRecetas.setEditable(esAdmin);
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreRecetaProperty());

        if (esAdmin) {
            colNombre.setCellFactory(TextFieldTableCell.forTableColumn());
            colNombre.setOnEditCommit(event -> {
                Receta r = event.getRowValue();
                r.setNombreReceta(event.getNewValue());
                recetaDAO.actualizar(r);
                cargarRecetas();
            });
            cargarTablasAdmin();
        }

        cargarRecetas();
    }

    // --- LÓGICA DE GESTIÓN DE TABLAS (ADMIN) ---
    private void cargarTablasAdmin() {
        String[] tablas = {"ingredientes_base", "usuarios"};
        TabPane subTabPane = new TabPane();
        for (String nombre : tablas) {
            TableView<ObservableList<String>> tv = new TableView<>();
            // Corrección: política de redimensionamiento para que no quede vacía
            tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            cargarTablaGenerica(tv, nombre);
            subTabPane.getTabs().add(new Tab(nombre, tv));
        }
        panelAdminStack.getChildren().setAll(subTabPane);
    }

    private void cargarTablaGenerica(TableView<ObservableList<String>> tabla, String nombreTabla) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + nombreTabla)) {

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(j)));
                tabla.getColumns().add(col);
            }
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) row.add(rs.getString(i));
                tabla.getItems().add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error en tabla " + nombreTabla + ": " + e.getMessage());
        }
    }

    // --- LÓGICA DE RECETAS Y TARJETAS ---
    @FXML
    public void abrirFormularioAnadir() {
        Receta nueva = new Receta();
        nueva.setNombreReceta("Nueva Receta");
        recetaDAO.guardar(nueva);
        cargarRecetas();
    }

    public void cargarRecetas() {
        List<Receta> lista = recetaDAO.listarTodos();
        tablaRecetas.getItems().setAll(lista);
        contenedorTarjetas.getChildren().clear();

        if (lista.isEmpty()) {
            contenedorTarjetas.getChildren().add(new Label("No hay recetas disponibles."));
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
            contenedorTarjetas.getChildren().add(lblTitulo);
        } catch (IOException e) {
            System.err.println("Error al cargar título: " + e.getMessage());
        }

        FlowPane flowSeccion = new FlowPane();
        for (Receta receta : lista) {
            flowSeccion.getChildren().add(cargarTarjeta(receta));
        }
        contenedorTarjetas.getChildren().add(flowSeccion);
    }

    private Node cargarTarjeta(Receta receta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ecochef/tarjeta-receta.fxml"));
            VBox tarjeta = loader.load();
            Label lbNombre = (Label) tarjeta.lookup("#lbNombre");
            Label lbTiempo = (Label) tarjeta.lookup("#lbTiempo");
            Button btnCheck = (Button) tarjeta.lookup("#btnCheck");
            Button btnEliminar = (Button) tarjeta.lookup("#btnEliminar");

            if (lbNombre != null) lbNombre.setText(receta.getNombreReceta().toUpperCase());
            if (lbTiempo != null) lbTiempo.setText("⏱ " + receta.getTiempoPreparacion() + " min");
            if (btnCheck != null) btnCheck.setOnAction(e -> mostrarAlerta("Ingredientes", recetaDAO.comprobarDisponibilidad(receta.getId())));

            if (btnEliminar != null) {
                if (!SesionActiva.esAdmin()) {
                    btnEliminar.setVisible(false);
                    btnEliminar.setManaged(false);
                } else {
                    btnEliminar.setOnAction(e -> {
                        recetaDAO.eliminar(receta.getId());
                        cargarRecetas();
                    });
                }
            }
            return tarjeta;
        } catch (IOException e) {
            return new Label("Error al cargar tarjeta");
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("EcoChef - Info");
        alert.setHeaderText(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    @FXML
    public void anadirFilaDinamica() {
        // 1. Obtenemos el TabPane interno que está dentro del StackPane
        TabPane subTabPane = (TabPane) panelAdminStack.getChildren().get(0);
        Tab tabSeleccionada = subTabPane.getSelectionModel().getSelectedItem();
        String nombreTabla = tabSeleccionada.getText();

        // 2. Insertamos una fila vacía en la base de datos
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Inserta un registro vacío (esto depende de si tu tabla permite campos nulos)
            stmt.executeUpdate("INSERT INTO " + nombreTabla + " VALUES ()");

            // 3. Recargamos la tabla para ver el nuevo registro
            TableView<ObservableList<String>> tabla = (TableView<ObservableList<String>>) tabSeleccionada.getContent();
            tabla.getItems().clear();
            cargarTablaGenerica(tabla, nombreTabla);

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No se pudo añadir fila: " + e.getMessage());
            alert.showAndWait();
        }
    }
}