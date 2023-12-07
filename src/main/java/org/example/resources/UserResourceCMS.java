package org.example.resources;

import org.example.objects.User;
import org.example.repositories.UserRepositoryCMS;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/usersCMS")
public class UserResourceCMS {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            return Response.ok().entity(UserRepositoryCMS.getInstance().getAllUsers()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("username") String username) {
        try {
            return Response.ok().entity(UserRepositoryCMS.getInstance().getUser(username)).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insert(User user) {
        try {
            if (UserRepositoryCMS.getInstance().userExists(user.getUsername())) {
                return Response.status(Response.Status.BAD_REQUEST).entity("User Already Exists").build();
            }
            UserRepositoryCMS.getInstance().insertUser(user);
            return Response.ok().entity("user created").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(User user) {
        try {
            if (!UserRepositoryCMS.getInstance().login(user.getUsername(), user.getPassword())) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Failed Login Attempt").build();
            }
            return Response.ok().entity("Successful Login Attempt").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("/{id}")
    @DELETE
    public Response delete(@PathParam("id") int id) {
        try {
            if (!UserRepositoryCMS.getInstance().userExistsById(id)) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID").build();
            }
            UserRepositoryCMS.getInstance().deleteUser(id);
            return Response.ok().entity("user deleted").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}