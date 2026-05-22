package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import org.example.ecochef.dao.AlimentoDAOImpl;
import org.example.ecochef.model.Alimento;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Controlador encargado de la gestión y visualización de alertas de caducidad.
 * Filtra y muestra los alimentos cuyo vencimiento es inminente (≤ 5 días).
 */
public class CaducidadController {

    @FXML
    private FlowPane flowCaducidades;

    private final AlimentoDAOImpl alimentoDAO = new AlimentoDAOImpl();

    /**
     * Inicializa el controlador cargando los datos al iniciar la vista.
     */
    @FXML
    public void initialize() {
        cargarAlimentosProximosACaducar();
    }

    /**
     * Lógica principal: obtiene todos los alimentos, calcula los días restantes
     * y genera dinámicamente tarjetas visuales para aquellos que caducan en menos de 5 días.
     */
    private void cargarAlimentosProximosACaducar() {
        flowCaducidades.getChildren().clear();
        List<Alimento> listaAlimentos = alimentoDAO.listarTodos();

        if (listaAlimentos == null || listaAlimentos.isEmpty()) {
            return;
        }

        LocalDate hoy = LocalDate.now();

        for (Alimento alimento : listaAlimentos) {
            if (alimento != null && alimento.getFechaCaducidad() != null) {

                // Cálculo de días usando ChronoUnit para obtener la diferencia real
                long diasRestantes = ChronoUnit.DAYS.between(hoy, alimento.getFechaCaducidad());

                if (diasRestantes <= 5) {
                    try {
                        // Carga dinámica de la vista (tarjeta) mediante FXML
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ecochef/tarjeta-caducidad.fxml"));
                        HBox tarjeta = loader.load();

                        Label lblTexto = (Label) tarjeta.lookup("#lblTextoCaducidad");

                        if (lblTexto != null) {
                            String mensaje = alimento.getNombre().toUpperCase() + " - Caduca en " + diasRestantes + " días";
                            lblTexto.setText(mensaje);
                        }

                        flowCaducidades.getChildren().add(tarjeta);

                    } catch (IOException e) {
                        System.err.println("Error al cargar la tarjeta: " + e.getMessage());
                    }
                }
            }
        }
    }
}