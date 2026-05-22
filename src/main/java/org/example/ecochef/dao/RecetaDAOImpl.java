package org.example.ecochef.dao;

import org.example.ecochef.model.Receta;
import org.example.ecochef.model.Usuario;
import org.example.ecochef.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Receta.
 * Gestiona operaciones de persistencia y lógica de consultas complejas
 * relacionadas con la disponibilidad de ingredientes en la despensa.
 */
public class RecetaDAOImpl implements Dao<Receta> {

    /**
     * Registra una nueva receta en la base de datos.
     */
    @Override
    public void guardar(Receta receta) {
        String sql = "INSERT INTO recetas (nombre, descripcion, tiempo_preparacion, id_usuario) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, receta.getNombreReceta());
            pstmt.setString(2, receta.getDescripcion());
            pstmt.setInt(3, receta.getTiempoPreparacion());
            // Asigna el ID del autor o un valor por defecto (1) si no existe usuario
            pstmt.setInt(4, (receta.getUsuario() != null) ? receta.getUsuario().getId() : 1);
            pstmt.executeUpdate();
            System.out.println("✅ Receta '" + receta.getNombreReceta() + "' guardada correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar receta: " + e.getMessage());
        }
    }

    /**
     * Actualiza los datos descriptivos de una receta existente.
     */
    @Override
    public void actualizar(Receta receta) {
        String sql = "UPDATE recetas SET nombre = ?, descripcion = ?, tiempo_preparacion = ? WHERE id_receta = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, receta.getNombreReceta());
            pstmt.setString(2, receta.getDescripcion());
            pstmt.setInt(3, receta.getTiempoPreparacion());
            pstmt.setInt(4, receta.getId());
            int filas = pstmt.executeUpdate();
            if (filas > 0) System.out.println("✅ Receta actualizada correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar receta: " + e.getMessage());
        }
    }

    /**
     * Elimina una receta del sistema mediante su ID.
     */
    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM recetas WHERE id_receta = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Receta eliminada (ID: " + id + ")");
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar receta: " + e.getMessage());
        }
    }

    /**
     * Obtiene una lista completa de todas las recetas registradas.
     */
    @Override
    public List<Receta> listarTodos() {
        List<Receta> lista = new ArrayList<>();
        String sql = "SELECT * FROM recetas";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Receta r = new Receta();
                r.setId(rs.getInt("id_receta"));
                r.setNombreReceta(rs.getString("nombre"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setTiempoPreparacion(rs.getInt("tiempo_preparacion"));

                Usuario autor = new Usuario();
                autor.setId(rs.getInt("id_usuario"));
                r.setUsuario(autor);
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar recetas: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca una receta específica por su ID.
     */
    @Override
    public Receta buscarPorId(int id) {
        String sql = "SELECT * FROM recetas WHERE id_receta = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Receta r = new Receta();
                r.setId(rs.getInt("id_receta"));
                r.setNombreReceta(rs.getString("nombre"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setTiempoPreparacion(rs.getInt("tiempo_preparacion"));
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Consulta compleja que verifica si los ingredientes de una receta están presentes
     * en la despensa del usuario, normalizando nombres con UPPER y TRIM.
     * @param idReceta ID de la receta a comprobar.
     * @return String con el reporte de disponibilidad (✅/❌).
     */
    public String comprobarDisponibilidad(int idReceta) {
        StringBuilder resultado = new StringBuilder();

        // Consulta SQL utilizando JOIN para relacionar ingredientes y subquery para comparar existencia
        String sql = "SELECT ib.nombre AS ingrediente, " +
                "CASE WHEN (SELECT COUNT(*) FROM alimentos a WHERE UPPER(TRIM(a.nombre)) = UPPER(TRIM(ib.nombre))) > 0 " +
                "THEN '✅' ELSE '❌' END AS estado " +
                "FROM recetas_ingredientes ri " +
                "JOIN ingredientes_base ib ON ri.id_alimento = ib.id_ingrediente_base " +
                "WHERE ri.id_receta = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idReceta);
            ResultSet rs = pstmt.executeQuery();

            boolean hayDatos = false;
            while (rs.next()) {
                hayDatos = true;
                resultado.append(rs.getString("estado"))
                        .append(" ")
                        .append(rs.getString("ingrediente"))
                        .append("\n");
            }

            if (!hayDatos) {
                return "No hay ingredientes definidos para esta receta.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Error en la base de datos: " + e.getMessage();
        }

        return resultado.toString();
    }

    /**
     * Filtra una lista de recetas basándose en un criterio de búsqueda textual,
     * utilizando la API de Streams para un filtrado eficiente y legible.
     */
    public List<Receta> filtrarRecetasPorNombre(List<Receta> listaCompleta, String textoBusqueda) {
        if (listaCompleta == null || textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return listaCompleta;
        }

        return listaCompleta.stream()
                .filter(r -> r.getNombreReceta().toLowerCase().contains(textoBusqueda.toLowerCase().trim()))
                .collect(java.util.stream.Collectors.toList());
    }
}