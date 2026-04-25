package com.sena.bancoweb.repository;

import com.sena.bancoweb.config.Conexion;
import com.sena.bancoweb.entity.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
/**
 * DAO para la gestión de usuarios y autenticación.
 * Implementa seguridad mediante hashing de contraseñas con BCrypt.
 */


public class UsuarioDAO {
    /**
     * Registra un nuevo usuario aplicando seguridad a su contraseña.
     * @param usuario Objeto con los datos del perfil.
     * @return true si el registro fue exitoso y el ID fue generado.
     */
    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios " +
                "(nombre, apellido, documento, email, password_hash) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Hash de la contraseña con bcrypt antes de guardar
            String hash = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt(12));

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getDocumento());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, hash);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) usuario.setIdUsuario(rs.getInt(1));
                }
            }
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }
    /**
     * Validar las credenciales de acceso de un usuario.
     * @param email Correo electrónico de identificación.
     * @param password Contraseña en texto plano suministrada por el usuario.
     * @return Usuario autenticado si las credenciales son correctas; null en caso contrario.
     */
    public Usuario validarLogin(String email, String password) {
        // Solo busca por email, luego verifica el hash
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashGuardado = rs.getString("password_hash");

                    // Verificar contraseña contra el hash
                    if (!BCrypt.checkpw(password, hashGuardado)) {
                        return null; // Contraseña incorrecta
                    }

                    Usuario user = new Usuario();
                    user.setIdUsuario(rs.getInt("id_usuario"));
                    user.setNombre(rs.getString("nombre"));
                    user.setApellido(rs.getString("apellido")); // ← fix: faltaba antes
                    user.setEmail(rs.getString("email"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en login: " + e.getMessage());
        }
        return null;
    }
    /**
     * Verifica la disponibilidad de un correo electrónico.
     * @param email Correo a consultar.
     * @return true si el correo ya está registrado en el sistema.
     */
    public boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar email: " + e.getMessage());
        }
        return false;
    }
}