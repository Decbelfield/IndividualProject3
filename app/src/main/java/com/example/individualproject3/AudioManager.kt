package com.example.individualproject3

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

object AudioManager {
    private var mediaPlayer: MediaPlayer? = null
    fun playBackgroundMusic(context: Context) {
        if (mediaPlayer == null) {
            try {
                mediaPlayer = MediaPlayer.create(context, R.raw.blinding_lights)
                mediaPlayer?.isLooping = true
                mediaPlayer?.start()
            } catch (e: Exception) {
                Log.e("AudioManager", "Error playing background music", e)
            }
        }
    }

    fun stopBackgroundMusic() {
        mediaPlayer?.let {
            it.stop()
            it.release()
        }
        mediaPlayer = null
    }

    fun pauseBackgroundMusic() {
        mediaPlayer?.pause()
    }
    fun resumeBackgroundMusic() {
        mediaPlayer?.start()
    }
    fun isMusicPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
}

