package org.example.objects;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

public class LowRes extends Thread{
    String fileName;
    String ext;
    String ProjPath;

    public LowRes(String fileName, String ext, String projPath){
        this.fileName = fileName;
        this.ext = ext;
        this.ProjPath = projPath;
    }

    public void run(){
        try {
            FFmpeg ffmpeg;
            FFprobe ffprobe;
            String input, output;
            ffmpeg = new FFmpeg("ffmpeg");
            ffprobe = new FFprobe("ffprobe");
            input = ProjPath + "/src/main/resources/videos/High/" + fileName;
            output = ProjPath + "/src/main/resources/videos/Low/" + fileName;

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(input)
                    .overrideOutputFiles(true)
                    .addOutput(output)
                    .setFormat(ext)
                    .disableSubtitle()
                    .setAudioChannels(1)
                    .setAudioCodec("aac")
                    .setAudioSampleRate(48_000)
                    .setAudioBitRate(32768)
                    .setVideoCodec("libx264")
                    .setVideoFrameRate(24, 1)
                    .setVideoResolution(480, 360)
                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                    .done();
            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            executor.createJob(builder).run();
        } catch (Exception ignored) {}
    }
}