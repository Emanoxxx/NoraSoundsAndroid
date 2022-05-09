package com.emanoxxxpc.nora.utils

import android.media.AudioManager
import android.media.MediaPlayer

class Player {
    companion object {
        fun playSound(archivos: MutableList<String>, id: String, host: String): Boolean {
            if (archivos.size == 0) {
                return false
            }

            val archivo = archivos[(0 until archivos.size).random()]
            val url = "http://${host}/${id}/${archivo}"
            MediaPlayer().apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(url)
                prepare() // might take long! (for buffering, etc)
                start()
            }
            return true
        }
    }
}