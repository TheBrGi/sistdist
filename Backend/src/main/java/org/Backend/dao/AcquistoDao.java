package org.Backend.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.Backend.model.Acquisto;
import org.Backend.model.Cliente;

/**
 * DAO for Acquisto
 */
@Stateless
public class AcquistoDao {
	@PersistenceContext(unitName = "Backend-persistence-unit")
	private EntityManager em;

	public void create(Acquisto entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Acquisto entity = em.find(Acquisto.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Acquisto findById(Long id) {
		return em.find(Acquisto.class, id);
	}

	public Acquisto update(Acquisto entity) {
		return em.merge(entity);
	}

	public List<Acquisto> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Acquisto> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT a FROM Acquisto a LEFT JOIN FETCH a.cliente LEFT JOIN FETCH a.partenza LEFT JOIN FETCH a.destinazione LEFT JOIN FETCH a.compagnia ORDER BY a.id",
						Acquisto.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}

	public List<Acquisto> query(Cliente c) {
		TypedQuery<Acquisto> query = em
				.createQuery(
						"SELECT DISTINCT a FROM Acquisto a LEFT JOIN FETCH a.cliente LEFT JOIN FETCH a.partenza LEFT JOIN FETCH a.destinazione LEFT JOIN FETCH a.compagnia "
						+ "WHERE a.cliente = :c ORDER BY a.tempo_acquisto",
						Acquisto.class);
		query.setParameter("c", c);
		
		return query.getResultList();
	}
}
