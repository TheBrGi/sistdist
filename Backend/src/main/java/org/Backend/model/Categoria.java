package org.Backend.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {

	@Id
	@Column(name = "nome", updatable = false, nullable = false)
	private String nome;
	private static final long serialVersionUID = 1L;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}	

	@XmlTransient
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "categoria", cascade = {CascadeType.ALL})
	private Set<Compagnia> compagnie;

	public Set<Compagnia> getCompagnie() {
		return compagnie;
	}

	public void setCompagnie(Set<Compagnia> compagnie) {
		this.compagnie = compagnie;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Categoria)) {
			return false;
		}
		Categoria other = (Categoria) obj;
		if (nome != null) {
			if (!nome.equals(other.nome)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}
}