package org.example.resources;

import org.example.objects.User;
import org.example.repositories.UserRepositoryCMS;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/usersCMS")
public class UserResourceCMS {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAll() throws SQLException {
        return new UserRepositoryCMS().getAllUsers();
    }

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("username") String username) throws SQLException {
        return new UserRepositoryCMS().getUser(username);
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(User user) throws SQLException {
        if (new UserRepositoryCMS().userExists(user.getUsername())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User Already Exists").build();
        }

        new UserRepositoryCMS().insertUser(user);
        return Response.ok().entity("user created").build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(User user) throws SQLException {
        new UserRepositoryCMS().updateUser(user);
        return Response.ok().entity("user updated").build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(User user) throws SQLException {
        if (!new UserRepositoryCMS().login(user.getUsername(), user.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Failed Login Attempt").build();
        }
        return Response.ok().entity("Successful Login Attempt").build();
    }


    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) throws SQLException {
        if (!new UserRepositoryCMS().userExistsById(id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID").build();
        }
        new UserRepositoryCMS().deleteUser(id);
        return Response.ok().entity("user deleted").build();
    }
}