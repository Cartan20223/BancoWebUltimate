package com.sena.bancoweb.controller;

import com.sena.bancoweb.entity.Usuario;
import com.sena.bancoweb.service.BancoService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/transferencia")
public class TransferenciaServlet extends HttpServlet {

    //  Declara el servicio
    private BancoService bancoService = new BancoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 2. Obtener la sesión y el usuario
        HttpSession session = request.getSession(false);
        Usuario usuarioRemitente = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        response.setContentType("application/json;charset=UTF-8");

        if (usuarioRemitente == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"ok\":false,\"error\":\"Sesión no válida\"}");
            return;
        }

        try {
            // 3. Capturar parámetros
            String numeroCuentaDestino = request.getParameter("cuentaDestino");
            String montoStr = request.getParameter("monto");

            if (numeroCuentaDestino == null || montoStr == null) {
                throw new Exception("Datos incompletos");
            }

            double monto = Double.parseDouble(montoStr);

            // 4. Ejecutar la lógica
            boolean exito = bancoService.realizarTransferencia(usuarioRemitente.getIdUsuario(), numeroCuentaDestino, monto);

            if (exito) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"ok\":true, \"mensaje\":\"Transferencia exitosa\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"ok\":false, \"error\":\"Fondos insuficientes o cuenta no existe\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"ok\":false, \"error\":\"Error al procesar la transferencia\"}");
        }
    }
}
