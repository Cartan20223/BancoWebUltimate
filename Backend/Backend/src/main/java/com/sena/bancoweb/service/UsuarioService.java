package com.sena.bancoweb.service;

import com.sena.bancoweb.entity.Cuenta;
import com.sena.bancoweb.entity.Usuario;
import com.sena.bancoweb.repository.CuentaDAO;
import com.sena.bancoweb.repository.UsuarioDAO;

public class UsuarioService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private CuentaDAO  cuentaDAO  = new CuentaDAO();

    public boolean registrarUsuarioConCuenta(Usuario usuario) {

        //  Verificar email duplicado antes de insertar
        if (usuarioDAO.existeEmail(usuario.getEmail())) {
            System.err.println("Email ya registrado: " + usuario.getEmail());
            return false;
        }

        //  Insertar usuario (el DAO hace el hash de la contraseña)
        if (!usuarioDAO.insertarUsuario(usuario)) {
            return false;
        }

        //  Crear cuenta con número único usando timestamp
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("AHO-" + (System.currentTimeMillis() % 900000 + 100000));
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setSaldo(0.0);
        cuenta.setIdUsuario(usuario.getIdUsuario());

        return cuentaDAO.crearCuenta(cuenta);
    }

    public Usuario login(String email, String password) {
        return usuarioDAO.validarLogin(email, password);
    }
}