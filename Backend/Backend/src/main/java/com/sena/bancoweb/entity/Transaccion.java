package com.sena.bancoweb.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Transaccion {
    private int idTransaccion;
    private double monto;
    private String tipoTransaccion; // "Deposito", "Retiro", "Transferencia"
    private LocalDateTime fechaTransaccion;
    private int idCuentaOrigen;
    private Integer idCuentaDestino; // Usamos Integer (objeto) porque puede ser null
}