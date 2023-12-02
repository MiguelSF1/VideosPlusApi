package org.example.resources;


import org.example.objects.User;
import org.example.repositories.UserRepository;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/users")
public class UserResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAll() throws SQLException {
        return UserRepository.getInstance().getAllUsers();
    }

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("username") String username) throws SQLException {
        return UserRepository.getInstance().getUser(username);
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insert(User user) throws SQLException {
        if (UserRepository.getInstance().userExists(user.getUsername())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User Already Exists").build();
        }

        UserRepository.getInstance().insertUser(user);
        return Response.ok().entity("user created").build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(User user) throws SQLException {
        if (!UserRepository.getInstance().login(user.getUsername(), user.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Failed Login Attempt").build();
        }
        return Response.ok().entity("Successful Login Attempt").build();
    }

    @Path("/{id}")
    @DELETE
    public Response delete(@PathParam("id") int id) throws SQLException {
        if (!UserRepository.getInstance().userExistsById(id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID").build();
        }
        UserRepository.getInstance().deleteUser(id);
        return Response.ok().entity("user deleted").build();
    }
}
