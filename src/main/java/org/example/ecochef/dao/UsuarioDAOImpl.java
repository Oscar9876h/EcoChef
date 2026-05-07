package org.example.ecochef.dao;

import org.example.ecochef.model.Usuario;
import org.example.ecochef.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements Dao<Usuario> {

    @Override
    public void guardar(Usuario usuario) {
        // Mantenemos "contraseña" con ñ porque así está en tu DB
        String sql = "INSERT INTO usuarios (nombre, email, contraseña, rol) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getContrasena());
            pstmt.setString(4, usuario.getRol());

            pstmt.executeUpdate();
            System.out.println("✅ ¡CONSEGUIDO! Usuario insertado correctamente en Workbench.");

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar usuario: " + e.getMessage());
        }
    }

    /**
     * MÉTODO LOGIN: Validado con tus nombres de columna reales
     */
    public Usuario login(String email, String password) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND contraseña = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Usuario user = new Usuario();

                // CORRECCIÓN AQUÍ: Usamos "idusuarios" como sale en tu foto de Workbench
                user.setId(rs.getInt("idusuarios"));

                user.setNombre(rs.getString("nombre"));
                user.setEmail(rs.getString("email"));
                user.setRol(rs.getString("rol"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en el login: " + e.getMessage());
        }
        return null;
    }

    @Override public void actualizar(Usuario usuario) {}
    @Override public void eliminar(int id) {}
    @Override public Usuario buscarPorId(int id) { return null; }
    @Override public List<Usuario> listarTodos() { return new ArrayList<>(); }
}