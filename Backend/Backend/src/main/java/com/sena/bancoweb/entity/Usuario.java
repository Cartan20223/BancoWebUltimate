package com.sena.bancoweb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String documento;
    private String email;
    private String password;
    private LocalDateTime fechaCreacion;




    // Constructor con parámetros (Para crear usuarios nuevos)
    public Usuario(String nombre, String apellido, String documento, String email, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.email = email;
        this.password = password;
        this.fechaCreacion = LocalDateTime.now();
    }




}