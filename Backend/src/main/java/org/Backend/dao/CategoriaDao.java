package org.Backend.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.Backend.model.Categoria;

/**
 * DAO for Categoria
 */
@Stateless
public class CategoriaDao {
	@PersistenceContext(unitName = "Backend-persistence-unit")
	private EntityManager em;

	public void create(Categoria entity) {
		em.persist(entity);
	}

	public void deleteById(String id) {
		Categoria entity = em.find(Categoria.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Categoria findById(String id) {
		return em.find(Categoria.class, id);
	}

	public Categoria update(Categoria entity) {
		return em.merge(entity);
	}

	public List<Categoria> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Categoria> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT c FROM Categoria c LEFT JOIN FETCH c.compagnie LEFT JOIN FETCH c.biglietti ORDER BY c.nome",
						Categoria.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
}
