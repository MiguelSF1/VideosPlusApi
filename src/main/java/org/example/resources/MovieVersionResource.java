package org.example.resources;


import org.example.objects.MovieVersion;
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
        return new MovieVersionRepository().getAllVersionsOfMovie();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MovieVersion getMovieVersion(@PathParam("id") int id) throws SQLException {
        return new MovieVersionRepository().getMovieVersion(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void insert(MovieVersion movieVersion) throws SQLException {
        new MovieVersionRepository().insertMovieVersion(movieVersion);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(MovieVersion movieVersion) throws SQLException {
        if (!new MovieVersionRepository().movieVersionExists(movieVersion.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(movieVersion.getId() + "Doesn't exist").build();
        }
        new MovieVersionRepository().updateMovieVersion(movieVersion);
        return Response.ok().entity(movieVersion).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) throws SQLException {
        if (id == 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID 0").build();
        }
        new MovieVersionRepository().deleteMovieVersion(id);
        return Response.ok().entity("Item has been deleted successfully.").build();
    }
}
