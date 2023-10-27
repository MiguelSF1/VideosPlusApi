package org.example.resources;


import org.example.objects.User;
import org.example.repositories.UserRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/users")
public class UserResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAll() throws SQLException {
        return new UserRepository().getAllUsers();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") int id) throws SQLException {
        return new UserRepository().getUser(id);
    }

    /*
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("username") String username) throws SQLException {
        return new UserRepository().getUser(username);
    } */

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void insert(User user) throws SQLException {
        new UserRepository().insertUser(user);
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(User user) throws SQLException {
        if (!new UserRepository().userExists(user.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(user.getId() + "Doesn't exist").build();
        }
        new UserRepository().updateUser(user);
        return Response.ok().entity(user).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) throws SQLException {
        if (id == 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID 0").build();
        }
        new UserRepository().deleteUser(id);
        return Response.ok().entity("Item has been deleted successfully.").build();
    }


    /*
    @Path("/{username}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteByUsername(@PathParam("username") String username) throws SQLException {
        if (!new UserRepository().userExists(username)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(username + "Doesn't exist").build();
        }
        new UserRepository().deleteUser(username);
        return Response.ok().entity("Item has been deleted successfully.").build();
    } */

}
