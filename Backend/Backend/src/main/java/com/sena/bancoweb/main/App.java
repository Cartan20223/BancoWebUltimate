package com.sena.bancoweb.main; //

import com.sena.bancoweb.entity.Usuario;
import com.sena.bancoweb.repository.UsuarioDAO;

public class App {
    public static void main(String[] args) {
        //
        Usuario userTest = new Usuario();
        userTest.setNombre("");
        //

        UsuarioDAO dao = new UsuarioDAO();
        dao.insertarUsuario(userTest);
    }
}