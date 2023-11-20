package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.objects.Movie;
import org.example.repositories.MovieRepository;


import java.sql.SQLException;
import java.util.List;

@Path("/movies")
public class MovieResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getAllMovies() throws SQLException {
        return new MovieRepository().getAllMovies();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Movie getMovie(@PathParam("id") int id) throws SQLException {
        return new MovieRepository().getMovie(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insert(Movie movie) throws SQLException {
        new MovieRepository().insertMovie(movie);
        return Response.ok().entity("movie added").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Movie movie) throws SQLException {
        if (!new MovieRepository().movieExists(movie.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(movie.getId() + " Doesn't exist").build();
        }
        new MovieRepository().updateMovie(movie);
        return Response.ok().entity(movie).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) throws SQLException {
        if (!new MovieRepository().movieExists(id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID").build();
        }
        new MovieRepository().deleteMovie(id);
        return Response.ok().entity("Item has been deleted successfully.").build();
    }

}
