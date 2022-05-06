package com.emanoxxxpc.nora.models

import com.emanoxxxpc.nora.api.ResponseError
import com.google.gson.Gson
import com.google.gson.JsonParser
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
    }
}

