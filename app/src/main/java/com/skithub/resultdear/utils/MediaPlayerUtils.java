package com.skithub.resultdear.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

/**
 * Created by myzupp on 26-02-2017.
 *
 * @author Darshan Parikh (parikhdarshan36@gmail.com)
 */

public class MediaPlayerUtils {

    private static MediaPlayer mediaPlayer;
    private static Listener listener;
    private static Handler mHandler;
    private static boolean buffering = true;

    /**
     * Get database instance
     * @return database handler instance
     */
    public static void getInstance() {
        if(mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        if(mHandler == null) {
            mHandler = new Handler();
        }
    }

    /**
     * Release mediaPlayer
     */
    public static void releaseMediaPlayer() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static void pauseMediaPlayer() {
        mediaPlayer.pause();
    }

    public static void playMediaPlayer() {
        mediaPlayer.start();
        mHandler.postDelayed(mRunnable, 100);
    }

    public static void applySeekBarValue(int selectedValue) {
        Log.d("seekPostion", String.valueOf(selectedValue));
        mediaPlayer.seekTo(selectedValue);
        mHandler.postDelayed(mRunnable, 100);
    }

    /**
     * Start mediaPlayer
     * @param audioUrl sd card media file
     * @throws IOException exception
     */
    public static void startAndPlayMediaPlayer(String audioUrl, final Listener listener) throws IOException {
        MediaPlayerUtils.listener = listener;
        getInstance();
        if(isPlaying()) {
            pauseMediaPlayer();
        }
        releaseMediaPlayer();
        getInstance();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(audioUrl);
        mediaPlayer.prepare();
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        mediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);

        mHandler.postDelayed(mRunnable, 100);
        playMediaPlayer();
    }

    public static boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public static int getTotalDuration() {
        Log.d("MediaDuration", String.valueOf(mediaPlayer.getDuration()));
        return mediaPlayer.getDuration();
    }

    static MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if(percent<99){
                buffering = true;
            }else{
                buffering = false;
            }
        }
    };

    private static MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            MediaPlayerUtils.releaseMediaPlayer();
            listener.onAudioComplete();
        }
    };

    private static Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (isPlaying()) {
                    mHandler.postDelayed(mRunnable, 100);
                    listener.onAudioUpdate(mediaPlayer.getCurrentPosition());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public interface Listener {
        void onAudioComplete();
        void onAudioUpdate(int currentPosition);
    }

}
