package org.example.objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StreamThread extends Thread {

    int videoId;
    String MoviePath, movieRes, movieFormat;

    public StreamThread(int videoId, String moviePath, String movieRes, String movieFormat){
        this.videoId = videoId;
        this.MoviePath = moviePath;
        this.movieRes = movieRes;
        this.movieFormat = movieFormat;
    }
    public void run() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String[] cmd;
        String projPath = System.getProperty("user.dir");
        String watchStreamPath = projPath + "/watch_stream.sh";
        cmd = new String[]{"sh", watchStreamPath, MoviePath, videoId + movieRes + movieFormat};
        processBuilder.command(cmd);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((reader.readLine()) != null) {}
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
