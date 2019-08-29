package org.Backend.rest;

import java.util.List;

import org.Backend.model.Biglietto;
import org.Backend.model.Cliente;

public class PurchaseReq {	

	private List<List<Biglietto>> paths;
	private Cliente cliente;

	public List<List<Biglietto>> getPaths() {
		return paths;
	}

	public void setPaths(List<List<Biglietto>> paths) {
		this.paths = paths;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
