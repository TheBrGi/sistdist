package org.Backend.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.Backend.model.Regione;

/**
 * DAO for Regione
 */
@Stateless
public class RegioneDao {
	@PersistenceContext(unitName = "Backend-persistence-unit")
	private EntityManager em;

	public void create(Regione entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Regione entity = em.find(Regione.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Regione findById(Long id) {
		return em.find(Regione.class, id);
	}

	public Regione update(Regione entity) {
		return em.merge(entity);
	}

	public List<Regione> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Regione> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT r FROM Regione r LEFT JOIN FETCH r.citta ORDER BY r.id",
						Regione.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
}
