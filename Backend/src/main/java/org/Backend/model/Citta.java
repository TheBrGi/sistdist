package org.Backend.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "citta")
@XmlRootElement
public class Citta implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	private static final long serialVersionUID = 1L;
	
	private String nome;

	@ManyToOne
	@JoinColumn(name = "regione_id")
	private Regione regione;
	
	@XmlTransient
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "partenza", cascade = {CascadeType.ALL})
	private Set<Biglietto> partenze;

	@XmlTransient
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "destinazione", cascade = {CascadeType.ALL})
	private Set<Biglietto> destinazioni;
	
	@XmlTransient
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "partenza", cascade = {CascadeType.ALL})
	private Set<Acquisto> partenzeA;

	@XmlTransient
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "destinazione", cascade = {CascadeType.ALL})
	private Set<Acquisto> destinazioniA;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Regione getRegione() {
		return regione;
	}

	public void setRegione(Regione regione) {
		this.regione = regione;
	}
	
	public Set<Biglietto> getPartenze() {
		return partenze;
	}

	public void setPartenze(Set<Biglietto> partenze) {
		this.partenze = partenze;
	}

	public Set<Biglietto> getDestinazioni() {
		return destinazioni;
	}

	public void setDestinazioni(Set<Biglietto> destinazioni) {
		this.destinazioni = destinazioni;
	}

	public Set<Acquisto> getPartenzeA() {
		return partenzeA;
	}

	public void setPartenzeA(Set<Acquisto> partenzeA) {
		this.partenzeA = partenzeA;
	}

	public Set<Acquisto> getDestinazioniA() {
		return destinazioniA;
	}

	public void setDestinazioniA(Set<Acquisto> destinazioniA) {
		this.destinazioniA = destinazioniA;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Citta)) {
			return false;
		}
		Citta other = (Citta) obj;
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