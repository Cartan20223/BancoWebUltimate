package com.sena.bancoweb.controller;

import com.sena.bancoweb.entity.Usuario;
import com.sena.bancoweb.repository.UsuarioDAO;
import com.sena.bancoweb.service.UsuarioService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Captura los datos del formulario
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String documento = request.getParameter("documento");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // 2. Validación básica
        if (nombre == null || nombre.isEmpty() ||
                apellido == null || apellido.isEmpty() ||
                documento == null || documento.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"ok\":false,\"error\":\"Todos los campos son requeridos\"}");
            return;
        }

        // CREAR EL OBJETO USUARIO
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellido(apellido);
        nuevoUsuario.setDocumento(documento);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password);

        // 4. Registrar en la base de datos
        boolean exito = usuarioService.registrarUsuarioConCuenta(nuevoUsuario);

        response.setContentType("application/json;charset=UTF-8");

        if (exito) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"ok\":true}");
        } else {
            // Detectar si fue por email duplicado para dar mensaje claro al usuario
            boolean emailDuplicado = new com.sena.bancoweb.repository.UsuarioDAO().existeEmail(email);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String errorMsg = emailDuplicado
                    ? "Este correo ya está registrado. Intenta con otro."
                    : "Error al crear la cuenta. Intenta nuevamente.";
            response.getWriter().write("{\"ok\":false,\"error\":\"" + errorMsg + "\"}");
        }
    }
}