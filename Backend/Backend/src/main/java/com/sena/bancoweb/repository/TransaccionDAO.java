package com.sena.bancoweb.repository;

import com.sena.bancoweb.config.Conexion;
import com.sena.bancoweb.entity.Transaccion;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * DAO para la gestión de movimientos financieros (transacciones).
 * Permite auditar y consultar el historial de operaciones de las cuentas.
 */
public class TransaccionDAO {
    /**
     * Registra un nuevo movimiento en la base de datos.
     * @param t Objeto transacción con monto, tipo y cuentas involucradas.
     * @return true si el registro fue exitoso.
     */
    public boolean registrar(Transaccion t) {
        String sql = "INSERT INTO transacciones (monto, tipo_transaccion, id_cuenta_origen, id_cuenta_destino) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, t.getMonto());
            ps.setString(2, t.getTipoTransaccion());
            ps.setInt(3, t.getIdCuentaOrigen());
            if (t.getIdCuentaDestino() != null) ps.setInt(4, t.getIdCuentaDestino());
            else ps.setNull(4, Types.INTEGER);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar transacción: " + e.getMessage());
            return false;
        }
    }
    /**
     * Obtiene el historial de movimientos de una cuenta, ordenado por fecha descendente.
     * Realiza una traducción lógica del tipo de transacción para facilitar la lectura al usuario.
     *
     * @param idCuenta Identificador de la cuenta a consultar.
     * @return Lista de transacciones realizadas o recibidas.
     */
    public List<Transaccion> obtenerPorCuenta(int idCuenta) {
        List<Transaccion> lista = new ArrayList<>();
        String sql = "SELECT * FROM transacciones WHERE id_cuenta_origen = ? OR id_cuenta_destino = ? ORDER BY fecha_transaccion DESC";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCuenta);
            ps.setInt(2, idCuenta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaccion t = new Transaccion();
                    t.setMonto(rs.getDouble("monto"));
                    // En el while(rs.next()) — reemplaza la línea de fechaTransaccion
                    Timestamp ts = rs.getTimestamp("fecha_transaccion");
                    if (ts != null) {
                        t.setFechaTransaccion(ts.toLocalDateTime());
                    } else {
                        t.setFechaTransaccion(LocalDateTime.now());
                    }

                    String tipoOriginal = rs.getString("tipo_transaccion");
                    int origenId = rs.getInt("id_cuenta_origen");
                    int destinoId = rs.getInt("id_cuenta_destino");

                    if (tipoOriginal.equals("Deposito")) {
                        t.setTipoTransaccion("Consignación");
                    } else if (origenId == idCuenta) {
                        t.setTipoTransaccion("Transferencia Enviada");
                    } else if (destinoId == idCuenta) {
                        t.setTipoTransaccion("Transferencia Recibida");
                    }

                    lista.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener transacciones: " + e.getMessage());
        }
        return lista;
    }
}