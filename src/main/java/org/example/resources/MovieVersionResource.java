package org.example.resources;


import jakarta.ws.rs.*;
import org.example.objects.LowRes;
import org.example.objects.MovieVersion;
import org.example.objects.StreamThread;
import org.example.repositories.MovieRepository;
import org.example.repositories.MovieVersionRepository;



import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

@Path("/movieVersions")
public class MovieVersionResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MovieVersion> getAll() throws SQLException {
        return MovieVersionRepository.getInstance().getAllVersionsOfMovies();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MovieVersion> getMovieVersions(@PathParam("id") int id) throws SQLException {
        return MovieVersionRepository.getInstance().getMovieVersions(id);
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) throws SQLException {
        if (!MovieVersionRepository.getInstance().movieVersionExists(id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID").build();
        }
        String movieVersionPath = MovieVersionRepository.getInstance().getMovieVersion(id).getMovieLink();
        new File(movieVersionPath).delete();
        MovieVersionRepository.getInstance().deleteMovieVersion(id);
        return Response.ok().entity("Movie Version deleted").build();
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("upload") InputStream is,
                               @FormDataParam("upload") FormDataContentDisposition formData,
                               @FormDataParam("movieId") String movieId) throws SQLException {

        if (is == null || !MovieRepository.getInstance().movieExists(Integer.parseInt(movieId))) {
            return Response.status(400).build();
        }
        String ext = getExt(formData.getFileName());
        if (ext.isEmpty()) {
            return Response.status(415).entity("need format").build();
        }
        String ProjPath = System.getProperty("user.dir");
        String fileLocation = ProjPath + "/src/main/resources/videos/High/" + formData.getFileName();
        String lowResFileLocation = ProjPath + "/src/main/resources/videos/Low/" + formData.getFileName();
        try {
            saveFile(is, fileLocation, lowResFileLocation, Integer.parseInt(movieId), ext);
            LowRes lowRes = new LowRes(formData.getFileName(), ext, ProjPath);
            lowRes.start();
            return Response.status(Response.Status.OK).entity("completed").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String getExt(String fileName){
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return fileName.substring(lastIndexOf).substring(1);
    }

    private void saveFile(InputStream is, String fileLocation, String lowResFileLocation, int movieId, String format) throws FileNotFoundException {
        try {
            OutputStream os = new FileOutputStream(fileLocation);
            byte[] buffer = new byte[256];
            int bytes;
            while ((bytes = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytes);
            }
            os.close();
            is.close();
            MovieVersionRepository.getInstance().insertMovieVersion(new MovieVersion(movieId, format, "high", fileLocation));
            MovieVersionRepository.getInstance().insertMovieVersion(new MovieVersion(movieId, format, "low", lowResFileLocation));
        } catch (IOException | SQLException ignored) {}
    }

    @POST
    @Path("/stream")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response movieStream(MovieVersion movieVersion) {
        String moviePath = movieVersion.getMovieLink();
        int movieId = movieVersion.getMovieId();
        String movieRes = movieVersion.getMovieResolution();
        String movieFormat = movieVersion.getMovieFormat();
        String URL;
        File f1 = new File("/tmp/hls/" + movieId + movieRes + movieFormat + "-0.ts");
        URL = "http://192.168.1.103:1234/" + movieId + movieRes + movieFormat + ".m3u8";
        if (f1.exists()) {
            return Response.status(200).entity(URL).build();
        }

        StreamThread streamThread = new StreamThread(movieId, moviePath, movieRes, movieFormat);
        streamThread.start();

        return Response.status(200).entity(URL).build();
    }
}
