package org.example.ecochef.dao;

import org.example.ecochef.model.Alimento;
import org.example.ecochef.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos (DAO) para la entidad Alimento.
 * Implementa las operaciones CRUD (Create, Read, Update, Delete) utilizando JDBC.
 */
public class AlimentoDAOImpl implements Dao<Alimento> {

    /**
     * Inserta un nuevo alimento en la base de datos.
     * Utiliza PreparedStatement para evitar inyecciones SQL.
     */
    @Override
    public void guardar(Alimento alimento) {
        String sql = "INSERT INTO alimentos (nombre, tipo, calorias, id_categoria_alimento, fecha_caducidad) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, alimento.getNombre());
            pstmt.setString(2, alimento.getTipo());
            pstmt.setInt(3, alimento.getCalorias());
            pstmt.setInt(4, alimento.getIdCategoriaAlimento());

            // Conversión de LocalDate a java.sql.Date para compatibilidad con MySQL
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

    /**
     * Recupera todos los registros de la tabla 'alimentos' y los convierte a objetos Alimento.
     */
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

                // Convertimos java.sql.Date de vuelta a LocalDate para el uso en la app
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

    /**
     * Actualiza los datos de un alimento existente identificado por su ID.
     */
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

    /**
     * Elimina un registro de la tabla 'alimentos' según su ID.
     */
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
        // Implementación pendiente según necesidades del proyecto
        return null;
    }

    /**
     * Consulta específica que devuelve los alimentos que caducan en los próximos 7 días.
     * Utiliza funciones nativas de SQL (CURDATE y DATE_ADD).
     */
    public List<Alimento> listarProximosACaducar() {
        List<Alimento> lista = new ArrayList<>();
        String sql = "SELECT * FROM alimentos WHERE fecha_caducidad <= DATE_ADD(CURDATE(), INTERVAL 7 DAY) ORDER BY fecha_caducidad ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Alimento a = new Alimento();
                a.setId(rs.getInt("id_alimentos"));
                a.setNombre(rs.getString("nombre"));
                a.setTipo(rs.getString("tipo"));
                a.setCalorias(rs.getInt("calorias"));

                Date fechaSql = rs.getDate("fecha_caducidad");
                if (fechaSql != null) {
                    a.setFechaCaducidad(fechaSql.toLocalDate());
                }
                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en filtro caducidad: " + e.getMessage());
        }
        return lista;
    }
}