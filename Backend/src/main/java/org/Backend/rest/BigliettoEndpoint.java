package org.Backend.rest;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.Backend.dao.BigliettoDao;
import org.Backend.dao.CittaDao;
import org.Backend.dao.ClienteDao;
import org.Backend.model.Acquisto;
import org.Backend.model.Biglietto;
import org.Backend.model.Citta;
import org.Backend.model.Cliente;

/**
 * 
 */
@Stateless
@Path("/bigliettos")
public class BigliettoEndpoint {

	@Inject
	private CittaDao cittaDao;

	@Inject
	private BigliettoDao bdao;

	@Inject
	private ClienteDao cdao;
	
	@POST
	@Consumes("application/json")
	public Response create(Biglietto entity) {
		bdao.create(entity);
		return Response
				.created(UriBuilder.fromResource(BigliettoEndpoint.class).path(String.valueOf(entity.getId())).build())
				.build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Biglietto entity = bdao.findById(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		bdao.deleteById(id);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		Biglietto entity;
		try {
			entity = bdao.findById(id);
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	@GET
	@Produces("application/json")
	public List<Biglietto> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
		final List<Biglietto> results = bdao.listAll(startPosition, maxResult);
		return results;
	}

	@POST
	@Path("/paths")
	@Produces("application/json")
	public Response paths(String p_d) {
		String[] params = p_d.split("&");
		String[] partenza = params[0].split(", ");
		String[] destinazione = params[1].split(", ");

		List<Citta> citta = cittaDao.listAll();
		Citta id1 = null;
		Citta id2 = null;
		for (Citta item : citta) {
			if (item.getNome().equals(partenza[0]) && partenza[1].equals(item.getRegione().getNome())) {
				id1 = item;
			}
			if (item.getNome().equals(destinazione[0]) && destinazione[1].equals(item.getRegione().getNome())) {
				id2 = item;
			}
		}
		List<List<Citta>> entities = bdao.ricerca(id1, id2, 2);
		if (entities == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entities).build();
	}

	@POST
	@Path("/findtickets")
	@Produces("application/json")
	public Response findTickets(SearchReq req) {
		String[] partenza = req.getDa().split(", ");
		String[] destinazione = req.getA().split(", ");

		List<Citta> citta = cittaDao.listAll();
		Citta id1 = null;
		Citta id2 = null;
		for (Citta item : citta) {
			if (item.getNome().equals(partenza[0]) && partenza[1].equals(item.getRegione().getNome())) {
				id1 = item;
			}
			if (item.getNome().equals(destinazione[0]) && destinazione[1].equals(item.getRegione().getNome())) {
				id2 = item;
			}
		}

		Date dal = req.getDal(); // default: new Date(System.currentTimeMillis());

		List<Biglietto> sol1 = bdao.findBiglietto1(id1, id2, dal, req.getCat());
		List<Object[]> sol2 = bdao.findBiglietto2(id1, id2, dal, req.getCat());
		List<Object[]> sol3 = bdao.findBiglietto3(id1, id2, dal, req.getCat());
		List<List<Biglietto>> soluzioni = new LinkedList<>();
		for (Biglietto ob : sol1) {
			List<Biglietto> b = new LinkedList<>();
			b.add((Biglietto) ob);
			soluzioni.add(b);
		}
		for (Object[] ob : sol2) {
			List<Biglietto> b = new LinkedList<>();
			for (int i = 0; i < ob.length; i++) {
				System.out.println("----" + ((Biglietto) ob[i]).getPartenza().getNome() + " "
						+ ((Biglietto) ob[i]).getDestinazione().getNome());
				b.add((Biglietto) ob[i]);
			}
			soluzioni.add(b);
		}
		for (Object[] ob : sol3) {
			List<Biglietto> b = new LinkedList<>();
			for (int i = 0; i < ob.length; i++) {
				System.out.println(ob[i]);
				b.add((Biglietto) ob[i]);
			}
			soluzioni.add(b);
		}

		if (soluzioni.size() == 0) {
			return Response.status(Status.NOT_FOUND).build();
		}

		return Response.ok(soluzioni).build();
	}

	@POST
	@Path("/query")
	@Produces("application/json")
	public Response query(String p_d) {
		String[] params = p_d.split("&");
		String[] partenza = params[0].split(", ");
		String[] destinazione = params[1].split(", ");

		List<Citta> citta = cittaDao.listAll();
		Citta id1 = null;
		Citta id2 = null;
		for (Citta item : citta) {
			if (item.getNome().equals(partenza[0]) && partenza[1].equals(item.getRegione().getNome())) {
				id1 = item;
			}
			if (item.getNome().equals(destinazione[0]) && destinazione[1].equals(item.getRegione().getNome())) {
				id2 = item;
			}
		}
		List<Biglietto> entities = bdao.findBiglietto(id1, id2);
		if (entities == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		return Response.ok(entities).build();
	}

	@POST
	@Path("/order")
	@Consumes("application/json")
	public Response order(PurchaseReq purchaseReq) {

		if (purchaseReq == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		Cliente c = cdao.findByEmail(purchaseReq.getCliente().getEmail());
		if (!purchaseReq.getCliente().getPassword().equals(c.getPassword())) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		List<Acquisto> acquisti;
		try {
			acquisti = bdao.purchase(purchaseReq);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
		}
		return Response.ok(acquisti).build();
	}

	@POST
	@Path("/gen")
	@Consumes("application/json")
	public Response generate() {
		List<Biglietto> b = bdao.listAll(0, 50);
		Long maxid = 0l;
		for (Biglietto biglietto : b) {
			if (maxid < biglietto.getId())
				maxid = biglietto.getId();
		}
		System.out.println(maxid
				+ "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		for (int i = 1; i < 40; i++)
			for (Biglietto biglietto : b) {
				maxid++;
				Biglietto bnew = new Biglietto();
				bnew.setId(maxid);
				bnew.setVersion(0);
				bnew.setTempoArrivo(BigliettoDao.addDays(biglietto.getTempoArrivo(), i));
				bnew.setTempoPartenza(BigliettoDao.addDays(biglietto.getTempoPartenza(), i));
				bnew.setCompagnia(biglietto.getCompagnia());
				bnew.setPartenza(biglietto.getPartenza());
				bnew.setDestinazione(biglietto.getDestinazione());
				bnew.setCosto(biglietto.getCosto());
				bnew.setOraArrivo(biglietto.getOraArrivo());
				bnew.setOraPartenza(biglietto.getOraPartenza());
				bnew.setQta(biglietto.getQta());
				bdao.insertWithQuery(bnew);
			}

		
		
		return Response.ok().build();
	}

//	@PUT
//	@Path("/{id:[0-9][0-9]*}")
//	@Consumes("application/json")
//	public Response update(@PathParam("id") Long id, Biglietto entity) {
//		if (entity == null) {
//			return Response.status(Status.BAD_REQUEST).build();
//		}
//		if (id == null) {
//			return Response.status(Status.BAD_REQUEST).build();
//		}
//		if (!id.equals(entity.getId())) {
//			return Response.status(Status.CONFLICT).entity(entity).build();
//		}
//		if (bdao.findById(id) == null) {
//			return Response.status(Status.NOT_FOUND).build();
//		}
//		try {
//			entity = bdao.update(entity);
//		} catch (OptimisticLockException e) {
//			return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
//		}
//
//		return Response.noContent().build();
//	}
}
