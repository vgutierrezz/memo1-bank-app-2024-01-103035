package com.aninfo.model;

import javax.persistence.*;

@Entity
public class Promo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double monto;

    private Long idTransaction;

    public Promo(){}

    public Promo(Double monto, Long idTransaction) {
        this.monto = monto;
        this.idTransaction = idTransaction;
    }

    public Long getId(){ return this.id;}

    public void setId(Long id){ this.id=id; }

    public Double getMonto(){ return this.monto;}

    public void setMonto(Double monto){ this.monto=monto;}

    //retorna el id asociado de la transaccion que genero el beneficio
    public Long getIdTransaction(){ return this.idTransaction;}

    //modifica el id asociado a la transaccion que genero el beneficio
    public void setIdTransaction(Long idTransaction){ this.idTransaction=idTransaction;}

}
