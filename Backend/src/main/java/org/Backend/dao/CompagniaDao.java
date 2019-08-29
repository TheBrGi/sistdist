package org.Backend.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.Backend.model.Compagnia;

/**
 * DAO for Compagnia
 */
@Stateless
public class CompagniaDao {
	@PersistenceContext(unitName = "Backend-persistence-unit")
	private EntityManager em;

	public void create(Compagnia entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Compagnia entity = em.find(Compagnia.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Compagnia findById(Long id) {
		return em.find(Compagnia.class, id);
	}

	public Compagnia update(Compagnia entity) {
		return em.merge(entity);
	}

	public List<Compagnia> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Compagnia> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT c FROM Compagnia c LEFT JOIN FETCH c.categoria LEFT JOIN FETCH c.biglietti LEFT JOIN FETCH c.acquisti ORDER BY c.id",
						Compagnia.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
}
