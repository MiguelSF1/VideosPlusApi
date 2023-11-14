package org.example.resources;


import org.example.objects.MovieVersion;
import org.example.repositories.MovieRepository;
import org.example.repositories.MovieVersionRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/movieVersions")
public class MovieVersionResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MovieVersion> getAll() throws SQLException {
        return new MovieVersionRepository().getAllVersionsOfMovies();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MovieVersion> getMovieVersions(@PathParam("id") int id) throws SQLException {
        return new MovieVersionRepository().getMovieVersions(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(MovieVersion movieVersion) throws SQLException {
        if (!new MovieRepository().movieExists(movieVersion.getMovieId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid MovieId").build();
        }
        new MovieVersionRepository().insertMovieVersion(movieVersion);
        return Response.ok().entity("Movie Version created").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(MovieVersion movieVersion) throws SQLException {
        if (!new MovieVersionRepository().movieVersionExists(movieVersion.getId()) || !new MovieRepository().movieExists(movieVersion.getMovieId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("invalid ID").build();
        }
        new MovieVersionRepository().updateMovieVersion(movieVersion);
        return Response.ok().entity(movieVersion).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) throws SQLException {
        if (!new MovieVersionRepository().movieVersionExists(id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID").build();
        }
        new MovieVersionRepository().deleteMovieVersion(id);
        return Response.ok().entity("Movie Version deleted").build();
    }
}
