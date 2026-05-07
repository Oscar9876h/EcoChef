package org.example.ecochef.dao;

import org.example.ecochef.model.Alimento;
import org.example.ecochef.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlimentoDAOImpl implements Dao<Alimento> {

    @Override
    public void guardar(Alimento alimento) {
        String sql = "INSERT INTO alimentos (nombre, tipo, calorias, id_categoria_) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, alimento.getNombre());
            pstmt.setString(2, alimento.getTipo());
            pstmt.setInt(3, alimento.getCalorias());
            pstmt.setInt(4, alimento.getIdCategoriaAlimento());

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
                a.setIdCategoriaAlimento(rs.getInt("id_categoria_"));
                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar alimentos: " + e.getMessage());
        }
        return lista;
    }

    @Override public void actualizar(Alimento alimento) {}
    @Override public void eliminar(int id) {}
    @Override public Alimento buscarPorId(int id) { return null; }
}