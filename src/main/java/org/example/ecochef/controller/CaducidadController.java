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

public class CaducidadController {

    @FXML
    private FlowPane flowCaducidades;

    // Usamos exactamente el mismo DAO que funciona en tu Despensa
    private final AlimentoDAOImpl alimentoDAO = new AlimentoDAOImpl();

    @FXML
    public void initialize() {
        cargarAlimentosProximosACaducar();
    }

    private void cargarAlimentosProximosACaducar() {
        flowCaducidades.getChildren().clear();

        // Traemos todos los alimentos tal y como hace la Despensa
        List<Alimento> listaAlimentos = alimentoDAO.listarTodos();

        if (listaAlimentos == null || listaAlimentos.isEmpty()) {
            System.out.println("⚠️ Alerta: No hay ningún alimento en la despensa.");
            return;
        }

        LocalDate hoy = LocalDate.now();

        for (Alimento alimento : listaAlimentos) {
            if (alimento != null && alimento.getFechaCaducidad() != null) {

                // Calculamos los días restantes usando la fecha del alimento
                long diasRestantes = ChronoUnit.DAYS.between(hoy, alimento.getFechaCaducidad());

                // REGLA: Si quedan 5 días o menos (o ya caducó), se muestra en la pantalla de alertas
                if (diasRestantes <= 5) {
                    try {
                        // 1. Cargamos el diseño FXML de la tarjeta
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ecochef/tarjeta-caducidad.fxml"));
                        HBox tarjeta = loader.load();

                        // 2. Buscamos el Label por su ID dentro del FXML
                        Label lblTexto = (Label) tarjeta.lookup("#lblTextoCaducidad");

                        // 3. Le asignamos los datos reales del alimento (¡con su nombre real!)
                        if (lblTexto != null) {
                            String mensaje = alimento.getNombre().toUpperCase() + " - Caduca en " + diasRestantes + " días";
                            lblTexto.setText(mensaje);
                        }

                        // 4. Añadimos la tarjeta al panel visual
                        flowCaducidades.getChildren().add(tarjeta);

                    } catch (IOException e) {
                        System.err.println("Error al cargar el FXML de la tarjeta: " + e.getMessage());
                    }
                }
            }
        }
    }
}