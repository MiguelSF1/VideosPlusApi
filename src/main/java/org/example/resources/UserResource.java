package org.example.resources;


import org.example.objects.User;
import org.example.repositories.UserRepository;

import javax.print.attribute.standard.Media;
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
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("username") String username) throws SQLException {
        return new UserRepository().getUser(username);
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(User user) throws SQLException {
        if (new UserRepository().userExists(user.getUsername())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User Already Exists").build();
        }

        User createdUser = new UserRepository().insertUser(user);
        return Response.ok().entity(createdUser).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(User user) throws SQLException {
        User updatedUser = new UserRepository().updateUser(user);
        return Response.ok().entity(updatedUser).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(User user) throws SQLException {
        if (!new UserRepository().login(user.getUsername(), user.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Failed Login Attempt").build();
        }
        return Response.ok().entity("Successful Login Attempt").build();
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
}
