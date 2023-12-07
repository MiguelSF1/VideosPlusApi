package org.example.objects;

import org.example.repositories.MovieVersionRepository;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class M3u8Thread extends Thread {
    String fileLocation;
    String ext;
    int movieId;
    String resolution;
    String filename;
    public M3u8Thread(String fileLocation, String ext, int movieId, String resolution, String filename) {
        this.fileLocation = fileLocation;
        this.ext = ext;
        this.movieId = movieId;
        this.resolution = resolution;
        this.filename = filename;
    }

    public void run() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String[] cmd;
        cmd = new String[] {"ffmpeg", "-i", fileLocation, "-codec:", "copy", "-start_number", "0", "-hls_time", "10", "-hls_list_size", "0", "-f", "hls", fileLocation.substring(0, fileLocation.length() - 4) + ext + ".m3u8"};
        processBuilder.command(cmd);
        try {
            Process process = processBuilder.start();
            process.waitFor();
            File ogCopy = new File(fileLocation);
            ogCopy.delete();
            MovieVersionRepository.getInstance().insertMovieVersion(new MovieVersion(movieId, ext, resolution, "http://192.168.1.103:1234/" + resolution + "/" + filename.substring(0, filename.length() - 4) + ext + "/" + filename.substring(0, filename.length() - 4) + ext + ".m3u8"));
        } catch (IOException | InterruptedException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
