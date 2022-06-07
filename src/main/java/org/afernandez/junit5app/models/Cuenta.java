package org.afernandez.junit5app.models;

import src.main.java.org.afernandez.junit5app.exceptions.DineroInsuficienteException;
import src.main.java.org.afernandez.junit5app.models.Banco;

import java.math.BigDecimal;

public class Cuenta {

    private String persona;
    private BigDecimal saldo;

    private Banco banco;

    public Cuenta(String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public void debito(BigDecimal total){
     //this.saldo = this.saldo.subtract(total);//restamos
     //si dejamos el subtract solo, al ser inmutable el bigdecimal no lo edita si no que crea un nuevo bigdecimal con ese cambio.
        //por eso le asignamos a saldo el valor nuevo.
        BigDecimal nuevoSaldo = this.saldo.subtract(total);
        //comparamos nuevo saldo para que no sea negativo
        if(nuevoSaldo.compareTo(BigDecimal.ZERO) < 0){
            throw new DineroInsuficienteException("Dinero Insuficiente"); //asignamos nuestra clase excepcion.
        }
        this.saldo = nuevoSaldo;

    }

    public void credito(BigDecimal total){
        this.saldo = this.saldo.add(total);//Añadimos
    }


    @Override //generar, sobreescribir metodo, equals.
    public boolean equals(Object obj) {
        //hacemos un cast para que sea la comprobacion por el valor.
        //comprobar que no sea nulos, y que sea un objeto Cuenta
        if(!(obj instanceof Cuenta)){
            return false;
        }
        Cuenta c = (Cuenta) obj;
        //comprobamos que no estén vacíos.
        if(this.persona == null || this.saldo == null){
            return false;
        }
        //aqui ya comprobamos si el valor del objeto es igual.
        return this.persona.equals(c.getPersona()) && this.getSaldo().equals(c.getSaldo());
    }
}
