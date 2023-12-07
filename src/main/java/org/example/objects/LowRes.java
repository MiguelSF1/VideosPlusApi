package org.example.objects;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

public class LowRes extends Thread {
    String input;
    String ext;
    String output;
    int movieId;
    String filename;

    public LowRes(String input, String ext, String output, int movieId, String filename) {
        this.input = input;
        this.ext = ext;
        this.output = output;
        this.movieId = movieId;
        this.filename = filename;
    }

    public void run() {
        try {
            FFmpeg ffmpeg;
            FFprobe ffprobe;
            ffmpeg = new FFmpeg("ffmpeg");
            ffprobe = new FFprobe("ffprobe");

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(input)
                    .overrideOutputFiles(true)
                    .addOutput(output)
                    .setFormat(ext)
                    .disableSubtitle()
                    .setVideoResolution(480, 360)
                    .done();
            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            executor.createJob(builder).run();
            M3u8Thread m3u8Thread = new M3u8Thread(output, ext, movieId, "low", filename);
            m3u8Thread.start();
        } catch (Exception ignored) {}
    }
}
