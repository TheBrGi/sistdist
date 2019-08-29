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

import org.Backend.dao.ClienteDao;
import org.Backend.model.Cliente;

/**
 * 
 */
@Stateless
@Path("/clientes")
public class ClienteEndpoint {
	@Inject
	private ClienteDao cdao;

	@POST
	@Consumes("application/json")
	public Response create(Cliente entity) {
		cdao.create(entity);

		return Response.ok(entity).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") String id) {
		Cliente entity = cdao.findById(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		cdao.deleteById(id);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id: .+}")
	@Produces("application/json")
	public Response findById(@PathParam("id") String id) {
		Cliente entity=cdao.findById(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	@POST
	@Path("/login")
	@Produces("application/json")
	public Response login(String e_p) {
		String[] params = e_p.split("&");
		Cliente entity;
		try {
			entity = cdao.findByEmail(params[0]);
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		if (!entity.getPassword().equals(params[1])) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
		return Response.ok(entity).build();
	}

	@GET
	@Produces("application/json")
	public List<Cliente> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
		final List<Cliente> results = cdao.listAll(startPosition, maxResult);
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") String id, Cliente entity) {
		if (entity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(entity.getEmail())) {
			return Response.status(Status.CONFLICT).entity(entity).build();
		}
		if (cdao.findById(id) == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		try {
			entity = cdao.update(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}
}
