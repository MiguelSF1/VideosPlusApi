package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.objects.Movie;
import org.example.repositories.MovieRepository;


import java.sql.SQLException;

@Path("/movies")
public class MovieResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMovies() throws SQLException {
        return Response.ok().entity(MovieRepository.getInstance().getAllMovies()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovie(@PathParam("id") int id) throws SQLException {
        return Response.ok().entity(MovieRepository.getInstance().getMovie(id)).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insert(Movie movie) throws SQLException {
        MovieRepository.getInstance().insertMovie(movie);
        return Response.ok().entity("movie added").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Movie movie) throws SQLException {
        if (!MovieRepository.getInstance().movieExists(movie.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(movie.getId() + " Doesn't exist").build();
        }
        MovieRepository.getInstance().updateMovie(movie);
        return Response.ok().entity(movie).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) throws SQLException {
        if (!MovieRepository.getInstance().movieExists(id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID").build();
        }
        MovieRepository.getInstance().deleteMovie(id);
        return Response.ok().entity("Item has been deleted successfully.").build();
    }

}
