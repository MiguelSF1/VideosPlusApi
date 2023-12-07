package org.example.resources;


import jakarta.ws.rs.*;
import org.example.objects.LowRes;
import org.example.objects.M3u8Thread;
import org.example.repositories.MovieRepository;
import org.example.repositories.MovieVersionRepository;



import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.*;
import java.sql.SQLException;

@Path("/movieVersions")
public class MovieVersionResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() throws SQLException {
        return Response.ok().entity(MovieVersionRepository.getInstance().getAllVersionsOfMovies()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieVersions(@PathParam("id") int id) throws SQLException {
        return Response.ok().entity(MovieVersionRepository.getInstance().getMovieVersions(id)).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) throws SQLException {
        if (!MovieVersionRepository.getInstance().movieVersionExists(id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID").build();
        }
        String movieVersionPath = MovieVersionRepository.getInstance().getMovieVersion(id).getMovieLink();
        File versionToDelete = new File(findFolder(movieVersionPath));
        if (!deleteDirectory(versionToDelete)) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
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
        String ProjPath = "/home/miguel/movieFiles/";
        File highPath = new File(ProjPath + "high/" + removeExt(formData.getFileName()) + getExt(formData.getFileName()));
        File lowPath = new File(ProjPath + "low/" + removeExt(formData.getFileName()) + getExt(formData.getFileName()));
        highPath.mkdir();
        lowPath.mkdir();
        String fileLocation = highPath + "/" + formData.getFileName();
        String lowResFileLocation = lowPath + "/" + formData.getFileName();
        try {
            saveFile(is, fileLocation, Integer.parseInt(movieId), formData.getFileName());
            LowRes lowRes = new LowRes(fileLocation, ext, lowResFileLocation, Integer.parseInt(movieId), formData.getFileName());
            lowRes.start();
            return Response.status(Response.Status.OK).entity("completed base upload").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    private String removeExt(String filename) {
        int lasIndexOf = filename.lastIndexOf(".");
        return filename.substring(0, lasIndexOf);
    }

    private String getExt(String fileName){
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return fileName.substring(lastIndexOf).substring(1);
    }

    private String findFolder(String path) {
        int lastIndexOf = path.lastIndexOf("/");
        int slashCount = 0;
        int startIndex = 0;
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == ('/')) {
                slashCount++;
                if (slashCount == 3) {
                    startIndex = i;
                    break;
                }
            }
        }

        return "/home/miguel/movieFiles" + path.substring(startIndex, lastIndexOf);
    }

    private void saveFile(InputStream is, String fileLocation, int movieId, String filename) throws FileNotFoundException {
        try {
            OutputStream os = new FileOutputStream(fileLocation);
            byte[] buffer = new byte[256];
            int bytes;
            while ((bytes = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytes);
            }
            os.close();
            is.close();

            M3u8Thread m3u8Thread = new M3u8Thread(fileLocation, getExt(fileLocation), movieId, "high", filename);
            m3u8Thread.start();
        } catch (IOException ignored) {}
    }

    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}

