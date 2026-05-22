package org.example.ecochef.dao;

import org.example.ecochef.model.Usuario;
import org.example.ecochef.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Usuario.
 * Gestiona el registro de nuevos usuarios y el proceso de autenticación (login).
 */
public class UsuarioDAOImpl implements Dao<Usuario> {

    /**
     * Registra un nuevo usuario en la base de datos.
     * @param usuario Objeto con los datos del nuevo perfil a crear.
     */
    @Override
    public void guardar(Usuario usuario) {
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
     * Valida las credenciales de un usuario.
     * @param email El correo electrónico proporcionado.
     * @param password La contraseña proporcionada.
     * @return El objeto Usuario si las credenciales son correctas, o null si fallan.
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

                // Mapeo de campos desde la base de datos al modelo
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

    // Métodos pendientes de implementación para futuras funcionalidades de gestión de usuarios
    @Override public void actualizar(Usuario usuario) {}
    @Override public void eliminar(int id) {}
    @Override public Usuario buscarPorId(int id) { return null; }
    @Override public List<Usuario> listarTodos() { return new ArrayList<>(); }
}