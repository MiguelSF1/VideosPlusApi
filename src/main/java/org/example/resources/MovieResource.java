package org.example.resources;

import org.example.objects.Movie;
import org.example.repositories.MovieRepository;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Movie getById(@PathParam("id") int id) throws SQLException {
        return new MovieRepository().getMovie(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void insert(Movie movie) throws SQLException {
        new MovieRepository().insertMovie(movie);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Movie movie) throws SQLException {
        if (!new MovieRepository().movieExists(movie.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(movie.getId() + "Doesn't exist").build();
        }
        new MovieRepository().updateMovie(movie);
        return Response.ok().entity(movie).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) throws SQLException {
        if (id == 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID 0").build();
        }
        new MovieRepository().deleteMovie(id);
        return Response.ok().entity("Item has been deleted successfully.").build();
    }

}
