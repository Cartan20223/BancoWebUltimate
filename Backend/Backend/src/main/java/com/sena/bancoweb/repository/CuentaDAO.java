package com.sena.bancoweb.repository;

import com.sena.bancoweb.config.Conexion;
import com.sena.bancoweb.entity.Cuenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase de acceso a datos (DAO) para la entidad Cuenta
 * Centraliza todas las operaciones CRUD sobre la tabla 'cuentas'
 */

public class CuentaDAO {
     // Registra una nueva cuenta en la base de datos
    public boolean crearCuenta(Cuenta cuenta) {
        String sql = "INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo, id_usuario) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cuenta.getNumeroCuenta());
            ps.setString(2, cuenta.getTipoCuenta());
            ps.setDouble(3, cuenta.getSaldo());
            ps.setInt(4, cuenta.getIdUsuario());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear cuenta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca la cuenta asociada a un usuario específico
     * @param idUsuario Identificador único del usuario
     * @return Objeto Cuenta si existe, null si no se encuentra
     */
    public Cuenta obtenerCuentaPorUsuario(int idUsuario) {
        String sql = "SELECT * FROM cuentas WHERE id_usuario = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cuenta cuenta = new Cuenta();
                    cuenta.setIdCuenta(rs.getInt("id_cuenta"));
                    cuenta.setNumeroCuenta(rs.getString("numero_cuenta"));
                    cuenta.setTipoCuenta(rs.getString("tipo_cuenta"));
                    cuenta.setSaldo(rs.getDouble("saldo"));
                    cuenta.setIdUsuario(rs.getInt("id_usuario"));
                    return cuenta;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cuenta: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza el saldo de una cuenta específica
     * @param idCuenta Identificador de la cuenta a modificar
     * @param nuevoSaldo El nuevo valor que tendrá el campo saldo
     * @return  true si la actualización fue exitosa
     */

    public boolean actualizarSaldo(int idCuenta, double nuevoSaldo) {
        String sql = "UPDATE cuentas SET saldo = ? WHERE id_cuenta = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, nuevoSaldo);
            ps.setInt(2, idCuenta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar saldo: " + e.getMessage());
            return false;
        }
    }
    /**
     * Busca los datos básicos de una cuenta mediante su número de cuenta.
     * @param numero El número de cuenta (String).
     * @return Objeto Cuenta con ID y Saldo cargados.
     */
    public Cuenta obtenerPorNumero(String numero) {
        String sql = "SELECT * FROM cuentas WHERE numero_cuenta = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cuenta c = new Cuenta();
                    c.setIdCuenta(rs.getInt("id_cuenta"));
                    c.setSaldo(rs.getDouble("saldo"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cuenta por número: " + e.getMessage());
        }
        return null;
    }
}