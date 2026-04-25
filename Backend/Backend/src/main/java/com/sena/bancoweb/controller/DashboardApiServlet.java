package com.sena.bancoweb.controller;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.Gson;
import com.sena.bancoweb.entity.Cuenta;
import com.sena.bancoweb.entity.Transaccion;
import com.sena.bancoweb.entity.Usuario;
import com.sena.bancoweb.repository.CuentaDAO;
import com.sena.bancoweb.repository.TransaccionDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/dashboard")
public class DashboardApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        try {
            // 1. Verificar sesión activa
            HttpSession session = request.getSession(false);
            Usuario user = (session != null)
                    ? (Usuario) session.getAttribute("usuario")
                    : null;

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"No autenticado\"}");
                return;
            }

            // 2. Obtener cuenta del usuario
            CuentaDAO cuentaDAO = new CuentaDAO();
            Cuenta cuenta = cuentaDAO.obtenerCuentaPorUsuario(user.getIdUsuario());

            if (cuenta == null || cuenta.getIdCuenta() == 0) {
                // Usuario existe pero no tiene cuenta asignada aún
                cuenta = new Cuenta();
                cuenta.setIdCuenta(0);
                cuenta.setSaldo(0.0);
                cuenta.setNumeroCuenta("SIN-ASIGNAR");
                cuenta.setTipoCuenta("Ahorros");
            }

            // 3. Obtener movimientos (lista vacía si no hay cuenta)
            List<Transaccion> movimientos = new ArrayList<>();
            if (cuenta.getIdCuenta() > 0) {
                TransaccionDAO txDAO = new TransaccionDAO();
                movimientos = txDAO.obtenerPorCuenta(cuenta.getIdCuenta());
            }

            // 4. Armar respuesta — usuario SIN contraseña
            Map<String, String> usuarioSeguro = new HashMap<>();
            usuarioSeguro.put("nombre",   user.getNombre());
            usuarioSeguro.put("apellido", user.getApellido() != null ? user.getApellido() : "");
            usuarioSeguro.put("email",    user.getEmail());

            Map<String, Object> data = new HashMap<>();
            data.put("usuario",     usuarioSeguro);
            data.put("cuenta",      cuenta);
            data.put("movimientos", movimientos);

            // 5. Serializar con soporte para LocalDateTime
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class,
                            (JsonSerializer<LocalDateTime>) (src, type, ctx) ->
                                    new JsonPrimitive(src.toString()))
                    .create();

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(data));

        } catch (Exception e) {

            System.err.println("Error en DashboardApiServlet: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Error interno del servidor\"}");
        }
    }
}