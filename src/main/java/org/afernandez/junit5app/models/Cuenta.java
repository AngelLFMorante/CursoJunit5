package org.afernandez.junit5app.models;

import java.math.BigDecimal;

public class Cuenta {

    private String persona;
    private BigDecimal saldo;

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
