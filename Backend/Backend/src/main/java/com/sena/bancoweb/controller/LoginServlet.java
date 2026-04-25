package com.sena.bancoweb.controller;

import com.google.gson.Gson;
import com.sena.bancoweb.entity.Usuario;
import com.sena.bancoweb.service.UsuarioService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet que gestiona el inicio de sesión de los usuarios
 * Mapeado a la ruta /LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private UsuarioService usuarioService = new UsuarioService();

    /**
     * Procesa la autenticación del usuario
     * @param request Contiene los parámetros "email" y "password"
     * @param response Retorna un JSON con los datos del usuario si tiene éxito
     */

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email    = request.getParameter("email").trim();
        String password = request.getParameter("password").trim();

        Usuario usuario = usuarioService.login(email, password);

        response.setContentType("application/json;charset=UTF-8");

        if (usuario != null) {
            request.getSession().setAttribute("usuario", usuario);


            Gson gson = new Gson();
            Map<String, Object> resp = new HashMap<>();
            resp.put("ok",     true);
            resp.put("nombre", usuario.getNombre());
            resp.put("email",  usuario.getEmail());
            response.getWriter().write(gson.toJson(resp));

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"ok\":false,\"error\":\"Credenciales incorrectas\"}");
        }
    }

}