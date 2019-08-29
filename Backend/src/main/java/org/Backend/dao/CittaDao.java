package org.Backend.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.Backend.model.Citta;

/**
 * DAO for Citta
 */
@Stateless
public class CittaDao {
	@PersistenceContext(unitName = "Backend-persistence-unit")
	private EntityManager em;

	public void create(Citta entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Citta entity = em.find(Citta.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Citta findById(Long id) {
		return em.find(Citta.class, id);
	}

	public Citta update(Citta entity) {
		return em.merge(entity);
	}

	public List<Citta> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Citta> findAllQuery = em.createQuery(
				"SELECT DISTINCT c FROM Citta c LEFT JOIN FETCH c.regione LEFT JOIN FETCH c.partenze LEFT JOIN FETCH c.destinazioni ORDER BY c.id",
				Citta.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}

	public List<Citta> listAll() {
		TypedQuery<Citta> findAllQuery = em.createQuery(
				"SELECT DISTINCT c FROM Citta c LEFT JOIN FETCH c.regione ORDER BY c.regione",
				Citta.class);

		return findAllQuery.getResultList();
	}
}
