package src.main.java.org.afernandez.junit5app.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.afernandez.junit5app.models.Cuenta;

public class Banco {
	private String nombre;
	private List<Cuenta> cuentas;

	public Banco() {
		//creamos la instancia de la ArrayList para que no nos dé null
		cuentas = new ArrayList<>();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Cuenta> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}

	//añadimos cuenta a la lista
	public void addCuenta(Cuenta cuenta){
		cuentas.add(cuenta);
		//hacemos la relacion bidereccional con Cuenta
		cuenta.setBanco(this);//le pasamos la misma referencia del banco
	}

	//metodo transferir
	public void transferir(Cuenta origen, Cuenta destino, BigDecimal total){
		origen.debito(total);//estamos transfiriendo y descontamos
		destino.credito(total);//depositamos el dinero de la transferencia a la otra cuenta
	}
}
