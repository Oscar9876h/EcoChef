package org.example.ecochef.dao;

import org.example.ecochef.model.Inventario;
import org.example.ecochef.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAOImpl implements Dao<Inventario> {

    @Override
    public void guardar(Inventario item) {
        // SQL basado en tu captura de pantalla de Workbench
        String sql = "INSERT INTO inventario (id_usuario, id_alimento, cantidad, fecha_caducidad) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, item.getIdUsuario());
            pstmt.setInt(2, item.getIdAlimento());
            pstmt.setDouble(3, item.getCantidadDisponible()); // Ajustado a tu modelo
            pstmt.setDate(4, Date.valueOf(item.getFechaCaducidad()));

            pstmt.executeUpdate();
            System.out.println("✅ Producto añadido al inventario correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar en inventario: " + e.getMessage());
        }
    }

    public List<Inventario> listarPorUsuario(int idUsuario) {
        List<Inventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Inventario item = new Inventario();
                item.setId(rs.getInt("id_inventario"));
                item.setIdUsuario(rs.getInt("id_usuario"));
                item.setIdAlimento(rs.getInt("id_alimento"));
                item.setCantidadDisponible(rs.getDouble("cantidad")); // Mapeo de DB a tu modelo
                item.setFechaCaducidad(rs.getDate("fecha_caducidad").toLocalDate());
                lista.add(item);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar inventario: " + e.getMessage());
        }
        return lista;
    }

    @Override public List<Inventario> listarTodos() { return new ArrayList<>(); }
    @Override public void actualizar(Inventario item) {}
    @Override public void eliminar(int id) {}
    @Override public Inventario buscarPorId(int id) { return null; }
}