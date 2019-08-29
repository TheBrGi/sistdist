package org.Backend.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "acquisti")
@XmlRootElement
public class Acquisto implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	private static final long serialVersionUID = 1L;

	private Long biglietto_id;

	private Date tempo_acquisto;

	private float costo;

	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "citta_partenza")
	private Citta partenza;

	@ManyToOne
	@JoinColumn(name = "citta_destinazione")
	private Citta destinazione;

	private Date tempoPartenza;

	private Date tempoArrivo;

	private int qta;

	@ManyToOne
	@JoinColumn(name = "compagnia_id")
	private Compagnia compagnia;

	public Long getBiglietto_id() {
		return biglietto_id;
	}

	public void setBiglietto_id(Long biglietto_id) {
		this.biglietto_id = biglietto_id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Citta getPartenza() {
		return partenza;
	}

	public void setPartenza(Citta partenza) {
		this.partenza = partenza;
	}

	public Citta getDestinazione() {
		return destinazione;
	}

	public void setDestinazione(Citta destinazione) {
		this.destinazione = destinazione;
	}

	public Date getTempoPartenza() {
		return tempoPartenza;
	}

	public void setTempoPartenza(Date tempoPartenza) {
		this.tempoPartenza = tempoPartenza;
	}

	public Date getTempoArrivo() {
		return tempoArrivo;
	}

	public void setTempoArrivo(Date tempoArrivo) {
		this.tempoArrivo = tempoArrivo;
	}

	public int getQta() {
		return qta;
	}

	public void setQta(int qta) {
		this.qta = qta;
	}

	public Compagnia getCompagnia() {
		return compagnia;
	}

	public void setCompagnia(Compagnia compagnia) {
		this.compagnia = compagnia;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTempo_acquisto() {
		return tempo_acquisto;
	}

	public void setTempo_acquisto(Date tempo_acquisto) {
		this.tempo_acquisto = tempo_acquisto;
	}

	public float getCosto() {
		return costo;
	}

	public void setCosto(float costo) {
		this.costo = costo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Acquisto)) {
			return false;
		}
		Acquisto other = (Acquisto) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}