package com.sena.bancoweb.controller;

import com.sena.bancoweb.entity.Usuario;
import com.sena.bancoweb.service.BancoService;
import com.sena.bancoweb.service.UsuarioService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controlador que gestiona las operaciones del deposito del dinero
 * mapeando la ruta deposito mediante el metodo POST
 */
@WebServlet("/deposito")
public class DepositoServlet extends HttpServlet {
    //Servicios para la logica de negocios y gestion de usuarios
    private BancoService bancoService = new BancoService();
    private UsuarioService usuarioService = new UsuarioService();

    /**
     *
     * @param request Contiene el parámetro "monto" y la sesión del usuario
     * @param response Retorna un JSON indicando éxito o error
     * @throws ServletException Si ocurre un error interno del servlet
     * @throws IOException Si hay errores en la escritura de la respuesta
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Verifica usuario auntenticado
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        response.setContentType("application/json;charset=UTF-8");

        if (usuario == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"ok\":false,\"error\":\"Sesión no válida\"}");
            return;
        }
        // Validar y capturar el monto enviado desde el formulario/cliente
        try {
            String montoStr = request.getParameter("monto");
            if (montoStr == null || montoStr.isEmpty()) {
                throw new Exception("Monto requerido");
            }
            double monto = Double.parseDouble(montoStr);

            boolean exito = bancoService.realizarDeposito(usuario.getIdUsuario(), monto);

            if (exito) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"ok\":true,\"mensaje\":\"Depósito exitoso\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"ok\":false,\"error\":\"No se pudo completar el depósito\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"ok\":false,\"error\":\"Error al procesar el depósito\"}");
        }
    }
}