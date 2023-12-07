package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.objects.Movie;
import org.example.objects.MovieVersion;
import org.example.repositories.MovieRepository;
import org.example.repositories.MovieVersionRepository;


import java.sql.SQLException;
import java.util.List;

@Path("/movies")
public class MovieResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMovies() {
        try {
            return Response.ok().entity(MovieRepository.getInstance().getAllMovies()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovie(@PathParam("id") int id) {
        try {
            return Response.ok().entity(MovieRepository.getInstance().getMovie(id)).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insert(Movie movie) {
        try {
            MovieRepository.getInstance().insertMovie(movie);
            return Response.ok().entity("movie added").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Movie movie) {
        try {
            if (!MovieRepository.getInstance().movieExists(movie.getId())) {
                return Response.status(Response.Status.BAD_REQUEST).entity(movie.getId() + " Doesn't exist").build();
            }
            MovieRepository.getInstance().updateMovie(movie);
            return Response.ok().entity(movie).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        try {
            if (!MovieRepository.getInstance().movieExists(id)) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID").build();
            }
            List<MovieVersion> movieVersions = MovieVersionRepository.getInstance().getMovieVersions(id);
            for (MovieVersion movieVersion : movieVersions) {
                if (!MovieVersionRepository.getInstance().deleteMovieVersion(movieVersion.getId())) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
            }
            MovieRepository.getInstance().deleteMovie(id);
            return Response.ok().entity("Item has been deleted successfully.").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
