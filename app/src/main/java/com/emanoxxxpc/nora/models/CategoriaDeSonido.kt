package com.emanoxxxpc.nora.models

import com.google.gson.annotations.SerializedName

data class CategoriaDeSonido(
    @SerializedName("name") val name: String,
    @SerializedName("comandos") val comandos: List<String>,
    @SerializedName("archivos") val archivos: List<String>,

    )
