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

    @Test
    void testReferenciaCuenta() {
        //vamos a crear un TDD, con lo que tendremos dos objetos en diferentes estados de memoria y no como referencia
        Cuenta cuenta = new Cuenta("Jhon Done", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("Jhon Done", new BigDecimal("8900.9997"));

        //assertNotEquals(cuenta2, cuenta);//comparamos que sean distintos...

        //queremos comprobar por valor.
        assertEquals(cuenta2, cuenta); //falla la primera vez por que son dos objetos diferentes aunque tengan el mismo valor.

        //ahora comprobamos por valor con lo que hemos tocado la clase cuenta, y hemos creado un metodo boolean equals.
        //hemos comprobado que sea el mismo objeto que Cuenta y comprobamos si el valor de los objetos son lo mismo, con
        //lo que en teoría estamos comprobado por referencia y no por dos objetos distintos.

    }

}