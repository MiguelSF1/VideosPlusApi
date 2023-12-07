package org.example.resources;


import jakarta.ws.rs.*;
import org.example.objects.MovieVersion;
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
        if (MovieVersionRepository.getInstance().deleteMovieVersion(id)) {
            return Response.ok().entity("Movie Version deleted").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
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
        if (!highPath.mkdir() || !lowPath.mkdir()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        String fileLocation = highPath + "/" + formData.getFileName();
        String lowResFileLocation = lowPath + "/" + formData.getFileName();
        try {
            saveFile(is, fileLocation);
            getLowRes(fileLocation, lowResFileLocation);
            getM3u8AndFinish(fileLocation, getExt(fileLocation), Integer.parseInt(movieId), "high", formData.getFileName());
            getM3u8AndFinish(lowResFileLocation, getExt(lowResFileLocation), Integer.parseInt(movieId), "low", formData.getFileName());
            return Response.status(Response.Status.OK).entity("completed upload").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void saveFile(InputStream is, String fileLocation) throws FileNotFoundException {
        try {
            OutputStream os = new FileOutputStream(fileLocation);
            byte[] buffer = new byte[256];
            int bytes;
            while ((bytes = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytes);
            }
            os.close();
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getLowRes(String input, String output) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String[] cmd;
        cmd = new String[] {"ffmpeg", "-i", input, "-vf", "scale=480:360", output};
        processBuilder.command(cmd);
        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void getM3u8AndFinish(String fileLocation, String ext, int movieId, String resolution, String filename) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String[] cmd;
        cmd = new String[] {"ffmpeg", "-i", fileLocation, "-codec:", "copy", "-start_number", "0", "-hls_time", "10", "-hls_list_size", "0", "-f", "hls", fileLocation.substring(0, fileLocation.length() - (ext.length() + 1)) + ext + ".m3u8"};
        processBuilder.command(cmd);
        try {
            Process process = processBuilder.start();
            process.waitFor();
            File ogCopy = new File(fileLocation);
            if (!ogCopy.delete()) {
                System.err.println("failed to delete original copy of movieVersion file but still created the needed m3u8 file. in: " + fileLocation);
            }
            String filenameWithoutExt = filename.substring(0, filename.length() - (ext.length() + 1));
            MovieVersionRepository.getInstance().insertMovieVersion(new MovieVersion(movieId, ext, resolution, "http://192.168.1.103:1234/" + resolution + "/" + filenameWithoutExt + ext + "/" + filenameWithoutExt + ext + ".m3u8"));
        } catch (IOException | InterruptedException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String removeExt(String filename) {
        int lastIndexOf = filename.lastIndexOf(".");
        return filename.substring(0, lastIndexOf);
    }

    private String getExt(String fileName){
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return fileName.substring(lastIndexOf + 1);
    }
}

