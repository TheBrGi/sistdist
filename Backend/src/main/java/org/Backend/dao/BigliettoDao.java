package org.Backend.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;

import org.Backend.model.Acquisto;
import org.Backend.model.Biglietto;
import org.Backend.model.Categoria;
import org.Backend.model.Citta;
import org.Backend.model.Cliente;
import org.Backend.rest.PurchaseReq;

/**
 * DAO for Biglietto
 */
@Stateless
public class BigliettoDao {
	@PersistenceContext(unitName = "Backend-persistence-unit")
	private EntityManager em;

	@Inject
	private AcquistoDao adao;

	public void create(Biglietto entity) {
		em.persist(entity);
	}

	public void insertWithQuery(Biglietto b) {
		em.createNativeQuery(
				"INSERT INTO biglietto (id, costo, qta, tempoarrivo, tempopartenza, version, compagnia_id, citta_destinazione, citta_partenza, oraarrivo, orapartenza) VALUES (?,?,?,?,?,?,?,?,?,?,?)")
				.setParameter(1, b.getId()).setParameter(2, b.getCosto()).setParameter(3, b.getQta())
				.setParameter(4, b.getTempoArrivo()).setParameter(5, b.getTempoPartenza())
				.setParameter(6, b.getVersion()).setParameter(7, b.getCompagnia().getId())
				.setParameter(8, b.getDestinazione().getId()).setParameter(9, b.getPartenza().getId())
				.setParameter(10, b.getOraArrivo()).setParameter(11, b.getOraPartenza())

				.executeUpdate();
	}

	public void merge(Biglietto entity) {
		em.merge(entity);
	}

	public void deleteById(Long id) {
		Biglietto entity = em.find(Biglietto.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Biglietto findById(Long id) {
		return em.find(Biglietto.class, id);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<Acquisto> purchase(PurchaseReq purchaseReq) {
		List<Acquisto> acquisti = new LinkedList<>();
		for (List<Biglietto> list : purchaseReq.getPaths()) {
			for (Biglietto biglietto : list) {
				acquisti.add(update(biglietto, purchaseReq.getCliente()));
			}
		}
		em.flush();
		return acquisti;
	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public Acquisto update(Biglietto entity, Cliente cliente) {
		Biglietto oldb = findById(entity.getId());
		int qta_acquistata = oldb.getQta() - entity.getQta();
		if (entity.getQta() < 0) {
			throw new InternalServerErrorException("Risorsa insufficiente");
		}
		Acquisto acquisto = null;
		if (entity.check(oldb)) {
			acquisto = new Acquisto();
			acquisto.setBiglietto_id(oldb.getId());
			acquisto.setCompagnia(oldb.getCompagnia());
			acquisto.setCliente(cliente);
			acquisto.setCosto(oldb.getCosto());
			acquisto.setPartenza(oldb.getPartenza());
			acquisto.setDestinazione(oldb.getDestinazione());
			acquisto.setQta(qta_acquistata);
			acquisto.setTempoArrivo(oldb.getTempoArrivo());
			acquisto.setTempoPartenza(oldb.getTempoPartenza());
			acquisto.setTempo_acquisto(new Date(System.currentTimeMillis()));

			adao.create(acquisto);

			em.merge(entity);

		} else {
			throw new InternalServerErrorException("Dati non corretti");
		}
		return acquisto;
	}

	public List<Biglietto> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Biglietto> findAllQuery = em.createQuery(
				"SELECT DISTINCT b FROM Biglietto b LEFT JOIN FETCH b.partenza LEFT JOIN FETCH b.destinazione LEFT JOIN FETCH b.compagnia ORDER BY b.id",
				Biglietto.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}

	public List<Biglietto> findBiglietto(Citta p, Citta d) {
		TypedQuery<Biglietto> findByIdQuery = em.createQuery(
				"SELECT DISTINCT b FROM Biglietto b WHERE b.partenza = :partenza AND b.destinazione = :destinazione ORDER BY b.id",
				Biglietto.class);
		findByIdQuery.setParameter("partenza", p);
		findByIdQuery.setParameter("destinazione", d);
		List<Biglietto> entities;
		try {
			entities = findByIdQuery.getResultList();
		} catch (NoResultException nre) {
			entities = null;
		}
		return entities;
	}

	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}

	public List<Biglietto> findBiglietto1(Citta p, Citta d, Date dal, Categoria categoria) {
		String cat = "";
		if (categoria != null) {
			cat = " AND b.compagnia.categoria = :cat";
		}
		TypedQuery<Biglietto> findByIdQuery = em.createQuery(
				"SELECT DISTINCT b FROM Biglietto b WHERE b.tempoPartenza = :d1 AND b.partenza = :partenza AND b.destinazione = :destinazione AND b.qta > 0"
						+ cat + " ORDER BY b.id",
				Biglietto.class);
		findByIdQuery.setParameter("partenza", p);
		findByIdQuery.setParameter("destinazione", d);
		findByIdQuery.setParameter("d1", dal);

		if (categoria != null) {
			findByIdQuery.setParameter("cat", categoria);
		}
		List<Biglietto> entities;
		try {
			entities = findByIdQuery.getResultList();
		} catch (NoResultException nre) {
			entities = null;
		}
		return entities;
	}

	public List<Object[]> findBiglietto2(Citta p, Citta d, Date dal, Categoria categoria) {
		String cat = "";
		if (categoria != null) {
			cat = " AND b.compagnia.categoria = :cat AND b1.compagnia.categoria = :cat";
		}
		Date nextDay = addDays(dal, 1);
		TypedQuery<Object[]> findByIdQuery = em.createQuery("SELECT DISTINCT b,b1 FROM Biglietto b,Biglietto b1 "
				+ "WHERE b.tempoPartenza = :d1 AND b1.tempoArrivo <= :d2 AND b.partenza = :partenza AND b.destinazione = b1.partenza AND b.destinazione != :destinazione "
				+ "AND b.tempoArrivo <= b1.tempoPartenza AND b.oraArrivo <= b1.oraPartenza AND b1.destinazione = :destinazione AND b.qta > 0 AND b1.qta >0"
				+ cat, Object[].class);
		findByIdQuery.setParameter("partenza", p);
		findByIdQuery.setParameter("destinazione", d);
		findByIdQuery.setParameter("d1", dal);
		findByIdQuery.setParameter("d2", nextDay);

		if (categoria != null) {
			findByIdQuery.setParameter("cat", categoria);
		}
		List<Object[]> entities;
		try {
			entities = findByIdQuery.getResultList();
		} catch (NoResultException nre) {
			entities = null;
		}
		return entities;
	}

	public List<Object[]> findBiglietto3(Citta p, Citta d, Date dal, Categoria categoria) {
		String cat = "";
		if (categoria != null) {
			cat = " AND b.compagnia.categoria = :cat AND b1.compagnia.categoria = :cat AND b2.compagnia.categoria = :cat";
		}

		Date nextDay = addDays(dal, 1);
		TypedQuery<Object[]> findByIdQuery = em
				.createQuery("SELECT DISTINCT b,b1,b2 FROM Biglietto b,Biglietto b1,Biglietto b2 "
						+ "WHERE b.tempoPartenza = :d1 AND b2.tempoArrivo <= :d2 AND b.partenza = :partenza AND b.destinazione != :destinazione AND b1.destinazione != :destinazione AND b.destinazione = b1.partenza AND b.tempoArrivo <= b1.tempoPartenza AND b.oraArrivo <= b1.oraPartenza "
						+ "AND b1.destinazione != :partenza AND b1.destinazione = b2.partenza AND b1.tempoArrivo <= b2.tempoPartenza AND b1.oraArrivo <= b2.oraPartenza AND b2.destinazione = :destinazione "
						+ "AND b.qta > 0 AND b1.qta >0 AND b2.qta>0" + cat, Object[].class);
		findByIdQuery.setParameter("partenza", p);
		findByIdQuery.setParameter("destinazione", d);
		findByIdQuery.setParameter("d1", dal);
		findByIdQuery.setParameter("d2", nextDay);

		if (categoria != null) {
			findByIdQuery.setParameter("cat", categoria);
		}
		List<Object[]> entities;
		try {
			entities = findByIdQuery.getResultList();
		} catch (NoResultException nre) {
			entities = null;
		}
		return entities;
	}

	public List<Citta> generaFigli(Citta c) {
		List<Citta> result = null;
		TypedQuery<Citta> findByPQuery = em.createQuery(
				"SELECT DISTINCT b.destinazione FROM Biglietto b WHERE b.partenza = :partenza", Citta.class);
		findByPQuery.setParameter("partenza", c);
		result = findByPQuery.getResultList();
		return result;

	}

	public List<List<Citta>> ricerca(Citta c1, Citta c2, int i) {
		List<List<Citta>> res = new LinkedList<>();
//		for (int j = 1; j <= i; j++) {
		LinkedList<List<Citta>> sol = new LinkedList<>();
		sol.add(new LinkedList<>());
		depthSearch(c1, c2, i, sol);
		for (List<Citta> list : sol) {
			if (list.size() > 1 && !res.contains(list))
				res.add(list);
		}

//		}

		return res;
	}

	private void depthSearch(Citta nodo, Citta c2, int j, LinkedList<List<Citta>> path) {
		List<Citta> npath = path.removeLast();
		npath.add(nodo);
		if (nodo.equals(c2)) {
			path.add(npath);
			return;
		} else if (j == 0) {
			return;
		}
		List<Citta> figli = generaFigli(nodo);
		for (Citta citta : figli) {
			path.add(new LinkedList<>(npath));
			depthSearch(citta, c2, j - 1, path);
		}

	}

}
