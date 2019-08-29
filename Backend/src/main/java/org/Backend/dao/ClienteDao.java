package org.Backend.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.Backend.model.Cliente;

/**
 * DAO for Cliente
 */
@Stateless
public class ClienteDao {
	@PersistenceContext(unitName = "Backend-persistence-unit")
	private EntityManager em;

	public void create(Cliente entity) {
		em.persist(entity);
	}

	public Cliente findByEmail(String email) {
		TypedQuery<Cliente> findByIdQuery = em.createQuery(
				"SELECT DISTINCT c FROM Cliente c WHERE c.email = :email ORDER BY c.email", Cliente.class);
		findByIdQuery.setParameter("email", email);
		Cliente entity = findByIdQuery.getSingleResult();
		return entity;
	}

	public void deleteById(String id) {
		Cliente entity = em.find(Cliente.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Cliente findById(String id) {
		return em.find(Cliente.class, id);
	}

	public Cliente update(Cliente entity) {
		return em.merge(entity);
	}

	public List<Cliente> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Cliente> findAllQuery = em.createQuery(
				"SELECT DISTINCT c FROM Cliente c LEFT JOIN FETCH c.acquisti ORDER BY c.email", Cliente.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
}
