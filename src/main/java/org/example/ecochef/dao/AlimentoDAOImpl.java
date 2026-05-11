package org.example.ecochef.dao;

import org.example.ecochef.model.Alimento;
import org.example.ecochef.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlimentoDAOImpl implements Dao<Alimento> {

    @Override
    public void guardar(Alimento alimento) {
        // SQL actualizado con la nueva columna fecha_caducidad
        String sql = "INSERT INTO alimentos (nombre, tipo, calorias, id_categoria_alimento, fecha_caducidad) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, alimento.getNombre());
            pstmt.setString(2, alimento.getTipo());
            pstmt.setInt(3, alimento.getCalorias());
            pstmt.setInt(4, alimento.getIdCategoriaAlimento());

            // Convertimos LocalDate a java.sql.Date para MySQL
            if (alimento.getFechaCaducidad() != null) {
                pstmt.setDate(5, Date.valueOf(alimento.getFechaCaducidad()));
            } else {
                pstmt.setNull(5, Types.DATE);
            }

            pstmt.executeUpdate();
            System.out.println("✅ Alimento '" + alimento.getNombre() + "' guardado correctamente.");

        } catch (SQLException e) {
            System.err.println("❌ Error al guardar alimento: " + e.getMessage());
        }
    }

    @Override
    public List<Alimento> listarTodos() {
        List<Alimento> lista = new ArrayList<>();
        String sql = "SELECT * FROM alimentos";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Alimento a = new Alimento();
                a.setId(rs.getInt("id_alimentos"));
                a.setNombre(rs.getString("nombre"));
                a.setTipo(rs.getString("tipo"));
                a.setCalorias(rs.getInt("calorias"));
                a.setIdCategoriaAlimento(rs.getInt("id_categoria_alimento"));

                // Leemos la fecha de la BD y la pasamos a LocalDate
                Date fechaSql = rs.getDate("fecha_caducidad");
                if (fechaSql != null) {
                    a.setFechaCaducidad(fechaSql.toLocalDate());
                }

                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar alimentos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void actualizar(Alimento alimento) {
        String sql = "UPDATE alimentos SET nombre=?, tipo=?, calorias=?, id_categoria_alimento=?, fecha_caducidad=? WHERE id_alimentos=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, alimento.getNombre());
            pstmt.setString(2, alimento.getTipo());
            pstmt.setInt(3, alimento.getCalorias());
            pstmt.setInt(4, alimento.getIdCategoriaAlimento());

            if (alimento.getFechaCaducidad() != null) {
                pstmt.setDate(5, Date.valueOf(alimento.getFechaCaducidad()));
            } else {
                pstmt.setNull(5, Types.DATE);
            }

            pstmt.setInt(6, alimento.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM alimentos WHERE id_alimentos = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Alimento eliminado.");
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar: " + e.getMessage());
        }
    }

    @Override
    public Alimento buscarPorId(int id) {
        return null;
    }
}