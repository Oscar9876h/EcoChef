package org.example.ecochef.dao;

import org.example.ecochef.model.Receta;
import org.example.ecochef.model.Usuario;
import org.example.ecochef.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecetaDAOImpl implements Dao<Receta> {

    @Override
    public void guardar(Receta receta) {
        String sql = "INSERT INTO recetas (nombre, descripcion, tiempo_preparacion, id_usuario) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, receta.getNombreReceta());
            pstmt.setString(2, receta.getDescripcion());
            pstmt.setInt(3, receta.getTiempoPreparacion());
            pstmt.setInt(4, (receta.getUsuario() != null) ? receta.getUsuario().getId() : 1);
            pstmt.executeUpdate();
            System.out.println("✅ Receta '" + receta.getNombreReceta() + "' guardada correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar receta: " + e.getMessage());
        }
    }

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
     * Comprueba disponibilidad cruzando correctamente los nombres de columnas de tus tablas.
     */
    public String comprobarDisponibilidad(int idReceta) {
        StringBuilder resultado = new StringBuilder();

        // EXPLICACIÓN DEL FIX:
        // ri.id_alimento -> Nombre en la tabla 'recetas_ingredientes'
        // a_req.id_alimentos -> Nombre en la tabla 'alimentos'
        String sql = "SELECT a_req.nombre AS ingrediente, " +
                "CASE WHEN a_disp.id_alimentos IS NOT NULL THEN '✅' ELSE '❌' END AS estado " +
                "FROM recetas_ingredientes ri " +
                "JOIN alimentos a_req ON ri.id_alimento = a_req.id_alimentos " +
                "LEFT JOIN alimentos a_disp ON a_req.id_alimentos = a_disp.id_alimentos " +
                "WHERE ri.id_receta = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idReceta);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                resultado.append(rs.getString("estado"))
                        .append(" ")
                        .append(rs.getString("ingrediente"))
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Error en la base de datos: " + e.getMessage();
        }

        return resultado.length() > 0 ? resultado.toString() : "No hay ingredientes definidos para esta receta.";
    }
}