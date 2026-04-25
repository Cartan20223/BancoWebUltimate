package com.sena.bancoweb.service;

import com.sena.bancoweb.config.Conexion;
import com.sena.bancoweb.entity.Cuenta;
import com.sena.bancoweb.entity.Transaccion;
import com.sena.bancoweb.repository.CuentaDAO;
import com.sena.bancoweb.repository.TransaccionDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BancoService {

    private CuentaDAO cuentaDAO = new CuentaDAO();
    private TransaccionDAO transaccionDAO = new TransaccionDAO();

    /**
     * Procesa un depósito de dinero en la cuenta de un usuario.
     * @param idUsuario ID del dueño de la cuenta.
     * @param monto Cantidad a depositar (debe ser > 0).
     * @return true si el proceso completo tuvo éxito.
     */

    public boolean realizarDeposito(int idUsuario, double monto) {
        if (monto <= 0) return false;

        Cuenta cuenta = cuentaDAO.obtenerCuentaPorUsuario(idUsuario);
        if (cuenta == null) return false;

        double nuevoSaldo = cuenta.getSaldo() + monto;
        // Actualizar saldo y registrar en el historial
        if (cuentaDAO.actualizarSaldo(cuenta.getIdCuenta(), nuevoSaldo)) {
            Transaccion t = new Transaccion();
            t.setMonto(monto);
            t.setTipoTransaccion("Deposito");
            t.setIdCuentaOrigen(cuenta.getIdCuenta());
            return transaccionDAO.registrar(t);
        }
        return false;
    }
    /**
     * Realiza una transferencia entre dos cuentas de forma atómica.
     * Si falla cualquier paso, se revierten todos los cambios (Rollback).
     *
     * @param idUsuarioOrigen ID del usuario que envía el dinero.
     * @param numCuentaDestino Número de cuenta de quien recibe.
     * @param monto Cantidad a transferir.
     * @return true si la transferencia se completó con éxito.
     */
    public boolean realizarTransferencia(int idUsuarioOrigen, String numCuentaDestino, double monto) {
        if (monto <= 0) return false;

        Connection con = null;
        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false); // Inicia la transacción manual

            //  Obtener y bloquear cuenta de ORIGEN
            Cuenta origen = null;
            String sqlOrigen = "SELECT id_cuenta, saldo FROM cuentas WHERE id_usuario = ? FOR UPDATE";
            try (PreparedStatement ps = con.prepareStatement(sqlOrigen)) {
                ps.setInt(1, idUsuarioOrigen);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        origen = new Cuenta();
                        origen.setIdCuenta(rs.getInt("id_cuenta"));
                        origen.setSaldo(rs.getDouble("saldo"));
                    }
                }
            }

            //  Leer cuenta DESTINO dentro de la misma transacción
            Cuenta destino = null;
            String sqlDestino = "SELECT id_cuenta, saldo FROM cuentas WHERE numero_cuenta = ? FOR UPDATE";
            try (PreparedStatement ps = con.prepareStatement(sqlDestino)) {
                ps.setString(1, numCuentaDestino);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        destino = new Cuenta();
                        destino.setIdCuenta(rs.getInt("id_cuenta"));
                        destino.setSaldo(rs.getDouble("saldo"));
                    }
                }
            }

            //  Validaciones
            if (origen == null || destino == null) {
                con.rollback();
                return false;
            }
            if (origen.getIdCuenta() == destino.getIdCuenta()) {
                con.rollback(); // No se puede transferir a la misma cuenta
                return false;
            }
            if (origen.getSaldo() < monto) {
                con.rollback(); // Saldo insuficiente
                return false;
            }

            //  Descontar del origen
            String sqlResta = "UPDATE cuentas SET saldo = ? WHERE id_cuenta = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlResta)) {
                ps.setDouble(1, origen.getSaldo() - monto);
                ps.setInt(2, origen.getIdCuenta());
                ps.executeUpdate();
            }

            //  Sumar al destino
            String sqlSuma = "UPDATE cuentas SET saldo = ? WHERE id_cuenta = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlSuma)) {
                ps.setDouble(1, destino.getSaldo() + monto);
                ps.setInt(2, destino.getIdCuenta());
                ps.executeUpdate();
            }

            //  Registrar la transacción
            String sqlTrans = "INSERT INTO transacciones (monto, tipo_transaccion, id_cuenta_origen, id_cuenta_destino) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sqlTrans)) {
                ps.setDouble(1, monto);
                ps.setString(2, "Transferencia");
                ps.setInt(3, origen.getIdCuenta());
                ps.setInt(4, destino.getIdCuenta());
                ps.executeUpdate();
            }

            con.commit(); // confirmar cambios
            return true;

        } catch (SQLException e) {
            System.err.println("Error en transferencia: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            // Limpieza: restablecer autocommit y cerrar conexión
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }
}