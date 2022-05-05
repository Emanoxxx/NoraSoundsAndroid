package com.emanoxxxpc.nora.models

data class CategoriaDeSonido(
    val nombre: String,
    val comandos: MutableList<String>? = null,
    val archivos: MutableList<String>? = null,
    val id: String? = null,

    )
