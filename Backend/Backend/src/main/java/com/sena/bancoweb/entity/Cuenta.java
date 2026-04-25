package com.sena.bancoweb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cuenta {
    private int idCuenta;
    private String numeroCuenta;
    private String tipoCuenta; // "Ahorros" o "Corriente"
    private double saldo;
    private int idUsuario; // La llave foránea
}