package org.afernandez.junit5app.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("Ángel", new BigDecimal("1000.12345"));
        String esperado = "Ángel";
        String actual = cuenta.getPersona();
        assertEquals(esperado, actual);
    }

    @Test
    void testSaldoCuenta(){
        Cuenta cuenta = new Cuenta("Ángel", new BigDecimal("1000.12345"));
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        // hay que darle el valor del double ya que bigDecimal lo necesita.(mira documentación si no te acueradas)
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        //para comprobar si es menor o mayor que cero se hace con el compareTo, ya que bigDecimal tiene su propia constante.
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0 );
    }

}