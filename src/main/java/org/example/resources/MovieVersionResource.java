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
    public Response getAll() {
        try {
            return Response.ok().entity(MovieVersionRepository.getInstance().getAllVersionsOfMovies()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieVersions(@PathParam("id") int id) {
        try {
            return Response.ok().entity(MovieVersionRepository.getInstance().getMovieVersions(id)).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        try {
            if (!MovieVersionRepository.getInstance().movieVersionExists(id)) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID").build();
            }
            if (MovieVersionRepository.getInstance().deleteMovieVersion(id)) {
                return Response.ok().entity("Movie Version deleted").build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("upload") InputStream is,
                               @FormDataParam("upload") FormDataContentDisposition formData,
                               @FormDataParam("movieId") String movieId) {
        String ProjPath = "/home/miguel/movieFiles/";
        String formFileName = formData.getFileName().replaceAll("\\s+","");
        File highPath = new File(ProjPath + "high/" + removeExt(formFileName) + getExt(formFileName));
        File lowPath = new File(ProjPath + "low/" + removeExt(formFileName) + getExt(formFileName));
        try {
            if (is == null || !MovieRepository.getInstance().movieExists(Integer.parseInt(movieId))) {
                return Response.status(400).build();
            }
            String ext = getExt(formFileName);
            if (ext.isEmpty()) {
                return Response.status(415).entity("need format").build();
            }
            if (!highPath.exists() && !lowPath.exists()) {
                if (!highPath.mkdir() || !lowPath.mkdir()) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
            }
            String fileLocation = highPath + "/" + formFileName;
            String lowResFileLocation = lowPath + "/" + formFileName;
            saveFile(is, fileLocation);
            if (is.available() == 0) {
                getLowRes(fileLocation, lowResFileLocation);
                getM3u8AndFinish(fileLocation, getExt(fileLocation), Integer.parseInt(movieId), "high", formFileName);
                getM3u8AndFinish(lowResFileLocation, getExt(lowResFileLocation), Integer.parseInt(movieId), "low", formFileName);
            }
            return Response.status(Response.Status.OK).entity("completed upload").build();
        } catch (IOException | InterruptedException | SQLException e) {
            if (!highPath.delete() || !lowPath.delete()) {
                System.err.println("failed to delete created files after exception in movie version creation at: " + highPath + " " + lowPath);
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void saveFile(InputStream is, String fileLocation) throws IOException {
        OutputStream os = new FileOutputStream(fileLocation);
        byte[] buffer = new byte[256];
        int bytes;
        while ((bytes = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
        os.close();
        is.close();
    }

    private void getLowRes(String input, String output) throws InterruptedException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String[] cmd;
        cmd = new String[] {"ffmpeg", "-i", input, "-vf", "scale=480:360", output};
        processBuilder.command(cmd);
        Process process = processBuilder.inheritIO().start();
        process.waitFor();
    }

    private void getM3u8AndFinish(String fileLocation, String ext, int movieId, String resolution, String filename) throws InterruptedException, IOException, SQLException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String[] cmd;
        cmd = new String[] {"ffmpeg", "-i", fileLocation, "-codec:", "copy", "-start_number", "0", "-hls_time", "10", "-hls_list_size", "0", "-f", "hls", fileLocation.substring(0, fileLocation.length() - (ext.length() + 1)) + ext + ".m3u8"};
        processBuilder.command(cmd);
        Process process = processBuilder.inheritIO().start();
        process.waitFor();
        File ogCopy = new File(fileLocation);
        if (!ogCopy.delete()) {
            System.err.println("failed to delete original copy of movieVersion file but still created the needed m3u8 file. in: " + fileLocation);
        }
        String filenameWithoutExt = filename.substring(0, filename.length() - (ext.length() + 1));
        MovieVersionRepository.getInstance().insertMovieVersion(new MovieVersion(movieId, ext, resolution, "http://192.168.1.103:1234/" + resolution + "/" + filenameWithoutExt + ext + "/" + filenameWithoutExt + ext + ".m3u8"));
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

