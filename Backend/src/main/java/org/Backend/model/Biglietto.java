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
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "biglietto")
@XmlRootElement
public class Biglietto implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "citta_partenza")
	private Citta partenza;

	@ManyToOne
	@JoinColumn(name = "citta_destinazione")
	private Citta destinazione;

	private Date tempoPartenza;

	private Date tempoArrivo;

	private String oraPartenza;

	private String oraArrivo;

	private float costo;

	private int qta;

	@ManyToOne
	@JoinColumn(name = "compagnia_id")
	private Compagnia compagnia;

	private static final long serialVersionUID = 1L;
	@Version
	@Column(name = "version")
	private int version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getOraPartenza() {
		return oraPartenza;
	}

	public void setOraPartenza(String oraPartenza) {
		this.oraPartenza = oraPartenza;
	}

	public String getOraArrivo() {
		return oraArrivo;
	}

	public void setOraArrivo(String oraArrivo) {
		this.oraArrivo = oraArrivo;
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
		if (!(obj instanceof Biglietto)) {
			return false;
		}
		Biglietto other = (Biglietto) obj;
		if (id != null) {
			if (!(id == other.id)) {
				return false;
			}
		}
		return true;
	}

	public boolean check(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Biglietto)) {
			return false;
		}
		Biglietto other = (Biglietto) obj;
		if (id != null) {
			if (!(id.equals(other.id) && costo == other.costo && compagnia.equals(other.compagnia)
					&& partenza.equals(other.partenza) && destinazione.equals(other.destinazione)
					&& tempoArrivo.equals(other.tempoArrivo) && tempoPartenza.equals(other.tempoPartenza)
					&& oraArrivo.equals(other.oraArrivo) && oraPartenza.equals(other.oraPartenza))) {
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}