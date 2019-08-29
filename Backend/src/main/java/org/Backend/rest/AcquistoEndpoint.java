package org.Backend.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
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

import org.Backend.dao.AcquistoDao;
import org.Backend.model.Acquisto;
import org.Backend.model.Cliente;

/**
 * 
 */
@Stateless
@Path("/acquistos")
public class AcquistoEndpoint {	
	@Inject
	private AcquistoDao adao;

	@POST
	@Consumes("application/json")
	public Response create(Acquisto entity) {
		adao.create(entity);
		return Response
				.created(
						UriBuilder.fromResource(AcquistoEndpoint.class)
								.path(String.valueOf(entity.getBiglietto_id()))
								.build()).build();
	}
	
	@POST
	@Path("/query")
	@Consumes("application/json")
	public Response query(Cliente c) {
		List<Acquisto> result= adao.query(c);
		return Response.ok(result).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Acquisto entity = adao.findById(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		adao.deleteById(id);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		Acquisto entity=adao.findById(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	@GET
	@Produces("application/json")
	public List<Acquisto> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {		
		final List<Acquisto> results = adao.listAll(startPosition, maxResult);
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, Acquisto entity) {
		if (entity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(entity.getBiglietto_id())) {
			return Response.status(Status.CONFLICT).entity(entity).build();
		}
		if (adao.findById(id) == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		try {
			entity = adao.update(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT)
					.entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}
	
	
	
}
