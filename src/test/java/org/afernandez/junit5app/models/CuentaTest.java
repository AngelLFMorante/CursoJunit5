package src.test.java.org.afernandez.junit5app.models;

import org.junit.jupiter.api.*;
import org.afernandez.junit5app.models.Cuenta;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import src.main.java.org.afernandez.junit5app.exceptions.DineroInsuficienteException;
import src.main.java.org.afernandez.junit5app.models.Banco;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) //podemos instanciar por clase o por metodos. con lo que se podria quitar el static en beforeAll (No se recomienda)
class CuentaTest {

    //@BeforeAll (metodo static) se ejecuta antes que todos los metodos solo 1 vez (Cuidado porque se crea antes de la clase)
    //@AfterAll (metodo static) se ejecuta una vez acabado todos los metodos solo 1 vez (Cuidado porque se crea antes de la clase)
    //@BeforeEach antes de empezar un metodo se llama a beforeEach
    //@AfterEach despues de que se haya finalizado el metodo, se llama a afterEach
    Cuenta cuenta;

    //para que se ejecute en diferentes sistemas operativos o versiones
    //@EnabledOnOs, @EnabledOnJre, @EnabledIfSystemProperty

    @BeforeEach //se ejecuta antes de cada metodo (metodo de ciclo de vida)
    void initMetodoTest(){
        //reutilizamos
        this.cuenta = new Cuenta("Ángel", new BigDecimal("1000.12345"));
        System.out.println("iniciando el método.");
    }

    @AfterEach //se ejecuta despues de cada metodo (metodo de ciclo de vida)
    void tearDown(){
        System.out.println("Finalizando el metodo de prueba");
    }

    //Los ALL solo se pueden utilizar en la clase principal, no se puede utilizar en las clases anidadas
    @BeforeAll //se crea antes de todo.
    static void beforeAll() {
        System.out.println("inicializando la clase test pero no las instancia");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("finalizando la clase test pero no las instancias");
    }

    @Nested
    class CuentaTestNombreSaldo{
        @Test
        @DisplayName("Probando nombre de la cuenta corriente!")
            //esto es para ponerle un nombre descriptivo para cuando salga en los métodos.
            //asi le damos un significado al metodo.
        void testNombreCuenta() {
            //cuenta = new Cuenta("Ángel", new BigDecimal("1000.12345"));
            String esperado = "Ángel";
            String actual = cuenta.getPersona();
            assertNotNull(actual, "la cuenta no peude ser nula");
            assertEquals(esperado, actual, "El nombre de la cuenta no es el que esperaba");
            //se pueden poner mensajes descriptivos, solo se va a mostrar cuando hay un error, aunque se puede mandar
            //de forma lambda, aunque hay que tener cuidado, que el mensaje puede salir aunque esté correcto.
            //le metemos un metodo con el string que va a invocar por debajo del assert, de si falla sale el mensaje, esto es lo mas correcto porque nos aseguramos que salga cuando falla.
        /*
            assertNotNull(actual, ()-> "la cuenta: " + actual + " no puede ser nula");
         */
        }

        @Test
        void testSaldoCuenta(){
            //cuenta = new Cuenta("Ángel", new BigDecimal("1000.12345"));
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            // hay que darle el valor del double ya que bigDecimal lo necesita.(mira documentación si no te acueradas)
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            //para comprobar si es menor o mayor que cero se hace con el compareTo, ya que bigDecimal tiene su propia constante.
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0 );
        }

        @Test
        void testReferenciaCuenta() {
            //vamos a crear un TDD, con lo que tendremos dos objetos en diferentes estados de memoria y no como referencia
            cuenta = new Cuenta("Jhon Done", new BigDecimal("8900.9997"));
            Cuenta cuenta2 = new Cuenta("Jhon Done", new BigDecimal("8900.9997"));

            //assertNotEquals(cuenta2, cuenta);//comparamos que sean distintos...

            //queremos comprobar por valor.
            assertEquals(cuenta2, cuenta); //falla la primera vez por que son dos objetos diferentes aunque tengan el mismo valor.

            //ahora comprobamos por valor con lo que hemos tocado la clase cuenta, y hemos creado un metodo boolean equals.
            //hemos comprobado que sea el mismo objeto que Cuenta y comprobamos si el valor de los objetos son lo mismo, con
            //lo que en teoría estamos comprobado por referencia y no por dos objetos distintos.

        }
    }

    @Nested
    class CuentaOperacionesTest{
        @Test
        void testDebitoCuenta() {
            //cuenta = new Cuenta("Ángel", new BigDecimal("1000.12345"));
            cuenta.debito(new BigDecimal(100)); // se le va a restar 1000 de los 1000
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900.12345", cuenta.getSaldo().toPlainString());
        }

        @Test
        void testCreditoCuenta() {
            //cuenta = new Cuenta("Ángel", new BigDecimal("1000.12345"));
            cuenta.credito(new BigDecimal(100)); // se le va a sumar 1000 de los 1000
            assertNotNull(cuenta.getSaldo());
            assertEquals(1100, cuenta.getSaldo().intValue());
            assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
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
    }


    @Test
    void testDineroInsuficienteExceptionCuenta() {
         //cuenta = new Cuenta("Ángel", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(DineroInsuficienteException.class, ()->{
           cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }



    @Test
    @Disabled //aparece en el reporte de que este test está deshabilitado, hasta que lo arreglemos.
    //Cuando hay algun método que todavia no lo hemos implementado, pero queremos seguir con otras pruebas..
    //utilizamos @Disabled, para obligar a fallar -> fail(); te aseguras que falla el método.
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


        //assertAll expresion lambda, en cada funcion lambda se le mete un assert
        //así por cada una que queremos comprobar, esto se hace para comprobar todas y que no se quede el test en un solo error.
        //Aquí dejo el ejemplo: es para colocar todas las comprobaciones que queremos hacer y que se reflejen todos los fallos.
        /*
        assertAll(()->{
            assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
            },
            ()->{
                assertEquals("3000", cuenta1.getSaldo().toPlainString());
            },
        }
         */
    }

    @Test //solo queremos que se ejecute cuando el desarrollo sea en dev,
    //el assertions es para afirmar nuestra prueba unitaria y el assumtions es para asumir cualquier valor de verdad true o false
    // si se cumple se hace la prueba, no queremos fallos y lo pasaria por alto
    void testSaldoCuentaDev(){
        //utilizamos un booleano para ver si estamos trabajando en desarrollo o produccion.
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        //asumimos que es verdadero, si no fuese así, se deshabilita y no pasa la prueba pero no falla.
        assumeTrue(esDev); //si es correcto funcionará todo el método

        //para crear pruebas de bloque y solo testear lo que queremos lo hacemos con assumingThat
        assumingThat(esDev, ()->{
            //cuenta = new Cuenta("Ángel", new BigDecimal("1000.12345"));
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        });

        // hay que darle el valor del double ya que bigDecimal lo necesita.(mira documentación si no te acueradas)
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        //para comprobar si es menor o mayor que cero se hace con el compareTo, ya que bigDecimal tiene su propia constante.
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0 );
    }

    //Lo bueno de los test que tienen innerClass y se pueden anidar clases
    //Para declarar que estamos anidando una clase se hace con @Nested
    //Si falla un metodo de la clase anidada, tambien falla el padre, y de ahi el abuelo.
    @Nested
    class SistemaOperativoTest{

        @Test
        @EnabledOnOs(OS.WINDOWS) //con OS nos salen todos los sitemas
        void testSoloWindows(){
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC}) //con OS nos salen todos los sitemas
            //Si le damos al metodo solamente haciendo run, lo va ha realizar aunque no sea linux ni mac por que se hace global con la clase.
        void testSoloLinuxMac(){
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows(){
        }
    }
    @Nested
    class JavaVersionTest{
        @Test
        @EnabledOnJre(JRE.JAVA_8) //es para versiones de java jdk8, esto es lo que saldría: Disabled on JRE version: 11, porque estoy con la version jdk 11
        void testSoloJDK8(){
        }

        @Test
        @EnabledOnJre(JRE.JAVA_11) //es para versiones de java jdk11, si funciona porque estoy con la version jdk 11
        void testSoloJDK11(){
        }

        //tambien hay para deshabilitarlo con @DisabledOnJre(JRE.JAVA_11)
    }
    @Nested
    class SystemPropertiesTest{
        @Test //comprobamos las caracteristicas de las propiedades que estamos utilizando y moldearlo a nuestra manera.
        void imprimirSystemProperties(){
            Properties properties = System.getProperties();
            properties.forEach((k,v) -> System.out.println(k + " : " +v));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = "15.0.1") //lo realiza cuando sea de clase, no vale solo con darle al run metodo ( no lo comprueba)
        void testJavaVersion() {
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "alfernandezm")
        void testUserName(){
        }
    }
    @Nested
    class VariableAmbienteTest{
        /*
    Para activar nuestra variable ENV hay que especificarlo arriba al lado del martillo, le damos click ***Test V editConfiguration donde pone build and run, vemos -ea pues le añadimos =  -DEV=dev para activarlo
     */

        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k,v)-> System.out.println(k + " : " + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk1.8.0_311.*" )
        void testJavaHome(){

        }
    }

    @DisplayName("Probando el metodo reiteradas veces")
    @RepeatedTest(value = 5, name = "Repeticion numero {currentRepetition} de {totalRepetitions}")  // lo que hacemos es que el método se repite 5 veces
    void testDebitoCuenta(RepetitionInfo info) {
        //cuenta = new Cuenta("Ángel", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100)); // se le va a restar 1000 de los 1000
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());

        //se puede tambien hacer cosas segun la repeticion que sea con el parametro RepetitionInfo
        if(info.getCurrentRepetition() == 3){
            //solo cuando esté en la repeticion 3, imprimirá
            System.out.println("estamos en la repeticion: "+ info.getCurrentRepetition());
        }
    }

    /*
    Para realizar test con diferentes escenarios con diferentes valores se realiza con @ParameterizedTest
     */

    @ParameterizedTest(name = "numero {index} ejecutando con el valor {argumentsWithNames}") //tiene que ir con valores, con @Source nos salen todos los valores que podemos hacer, CSV, Value, etc..
    @ValueSource(strings = {"100", "200", "300", "500", "700", "1000"}) //metemos valores para ver como se comporta
    //cuando trabajamos con decimales es mejor con strings, porque con double se pierde mucha precision.
    void testDebitoCuenta(String total) { // el parametro debe ser igual que el de valueSource
        cuenta.debito(new BigDecimal(total));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); //vamos a comprobar si restandole todos los valores que tenemos el credito es mayor a 0
    }



}