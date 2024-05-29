package com.aninfo.model;

import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;

@Entity
public class Transaction {
    @Id // defino que esta será la primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // El cbu será autogenerado y secuencial
    private Long id;

    private String tipo;

    private Double monto;

    private Long accountCbu; // referencia al cbu de la cuenta a la que está asociada

    public Transaction() {
    }

    public Transaction(String tipo, Double monto, Long accountCbu) {
        this.tipo = tipo;
        this.monto = monto;
        this.accountCbu = accountCbu;
    }

    // getId retorna el id de una transacción
    public Long getId() {
        return id;
    }

    // setId modifica el id de la transacción
    public void setId(Long id) {
        this.id = id;
    }

    // getMonto retorna el monto de una transacción
    public Double getMonto() {
        return monto;
    }

    // setMonto modifica el monto de una transacción
    public void setMonto(Double monto) {
        this.monto = monto;
    }

    // getTipo retorna el tipo de transacción: deposito o extracción
    public String getTipo() {
        return tipo;
    }

    // setTipo modifica el tipo de extracción
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // getAccount retorna el cbu de la cuenta a la que pertenece la transacción
    public Long getAccountCbu() {
        return accountCbu;
    }

    // setAccount modifica el valor del cbu de la cuenta a la que pertenece la
    // transaccion
    public void setAccount(Long accountCbu) {
        this.accountCbu = accountCbu;
    }

}