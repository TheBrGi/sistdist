package org.Backend.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.Backend.dao.CittaDao;
import org.Backend.model.Citta;

/**
 * 
 */
@Stateless
@Path("/citta")
public class CittaEndpoint {
	@Inject
	private CittaDao cdao;

	@POST
	@Consumes("application/json")
	public Response create(Citta entity) {
		cdao.create(entity);
		return Response.created(
				UriBuilder.fromResource(CittaEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Citta entity = cdao.findById(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		cdao.deleteById(id);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {		
		Citta entity;
		try {
			entity = cdao.findById(id);
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
	public List<Citta> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		final List<Citta> results = cdao.listAll(startPosition, maxResult);
		return results;
	}
	
	@GET
	@Path("/get")
	@Produces("application/json")
	public List<Citta> listAll() {
		final List<Citta> results = cdao.listAll();
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, Citta entity) {
		if (entity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(entity.getId())) {
			return Response.status(Status.CONFLICT).entity(entity).build();
		}
		if (cdao.findById(id) == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		try {
			entity = cdao.update(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT)
					.entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}
}
