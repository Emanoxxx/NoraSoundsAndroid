package com.emanoxxxpc.nora.models

import android.app.Activity
import android.media.AudioManager
import android.media.MediaPlayer
import android.widget.Toast
import com.emanoxxxpc.nora.RV_Sonido_Adapter
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

data class CategoriaDeSonido(
    val nombre: String,
    val comandos: MutableList<String>? = null,
    val archivos: MutableList<String>? = null,
    val id: String? = null,

    ){
    companion object {
        fun fromJSON(jsonString: String): CategoriaDeSonido {

            val gson = Gson()
            val parser = JsonParser()

            val mJson = parser.parse(jsonString)
            val categoriaDeSonido = gson.fromJson(mJson, CategoriaDeSonido::class.java)

            return categoriaDeSonido;
        }
        fun playSound(archivos: MutableList<String>,id: String,host:String):Boolean{
            if (archivos!!.size==0){
                return false
            }

            var archivo= archivos!![(0 until archivos!!.size).random()]
            val url = "http://${host}/${id}/${archivo}"
            val mediaPlayer: MediaPlayer? = MediaPlayer().apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(url)
                prepare() // might take long! (for buffering, etc)
                start()
                }
            return true
        }


    }
}

