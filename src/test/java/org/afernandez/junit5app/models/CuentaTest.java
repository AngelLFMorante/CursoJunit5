package src.test.java.org.afernandez.junit5app.models;

import org.junit.jupiter.api.Test;
import org.afernandez.junit5app.models.Cuenta;
import src.main.java.org.afernandez.junit5app.exceptions.DineroInsuficienteException;
import src.main.java.org.afernandez.junit5app.models.Banco;

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

    @Test
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("Ángel", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100)); // se le va a restar 1000 de los 1000
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Ángel", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100)); // se le va a sumar 1000 de los 1000
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteExceptionCuenta() {
        Cuenta cuenta = new Cuenta("Ángel", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(DineroInsuficienteException.class, ()->{
           cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    void testTransferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta ("Jhon Done", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta ("Jhon Done", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());
    }

    @Test
    void testRelacionBancoCuentas() {
        Cuenta cuenta1 = new Cuenta ("Jhon Done", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta ("Ángel", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        //añadimos las cuentas para que tenga la relacion
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());

        //comprobamos que efectivamente banco getCuentas tenga 2 cuentas
        assertEquals(2, banco.getCuentas().size());

        //ahora comprobamos la relacion de las cuentas
        assertEquals("Banco del Estado", cuenta1.getBanco().getNombre());
        assertEquals("Banco del Estado", cuenta2.getBanco().getNombre());
        //comprobamos con el Stream que existe esa persona en esa cuenta, y lo buscamos con el primer resultado que es un Optional
        assertEquals("Ángel", banco.getCuentas()
                .stream()
                .filter(c -> c.getPersona().equals("Ángel"))
                .findFirst()
                .get()
                .getPersona());
        //tambien se puede buscar de otra manera
        assertTrue(banco.getCuentas()
                .stream()
                .filter(c -> c.getPersona().equals("Ángel"))
                .findFirst()
                .isPresent());
        //aunque nso da warning para que lo pongamos con anyMatch para ver que si hay algun resultado.
        // banco.getCuentas().stream().anyMatch(c -> c.getPersona().equals("Ángel")));
    }
}