package org.example.ecochef.dao;

import org.example.ecochef.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl {

    public List<String> obtenerNombresCategorias() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nombre_categoria FROM categorias_alimentos ORDER BY nombre_categoria ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("nombre_categoria"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener categorías: " + e.getMessage());
        }
        return lista;
    }

    public int obtenerIdPorNombre(String nombre) {
        String sql = "SELECT idcategoria_alimento FROM categorias_alimentos WHERE nombre_categoria = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("idcategoria_alimento");
        } catch (SQLException e) { e.printStackTrace(); }
        return 1;
    }

}
