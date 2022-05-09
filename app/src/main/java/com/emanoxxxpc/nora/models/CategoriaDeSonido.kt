package com.emanoxxxpc.nora.models

import android.media.AudioManager
import android.media.MediaPlayer
import com.google.gson.Gson
import com.google.gson.JsonParser

data class CategoriaDeSonido(
    val nombre: String,
    val comandos: MutableList<String>? = null,
    val archivos: MutableList<String>? = null,
    val id: String? = null,

    )

