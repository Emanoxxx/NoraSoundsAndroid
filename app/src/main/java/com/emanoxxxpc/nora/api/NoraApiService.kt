package com.emanoxxxpc.nora.api

import com.emanoxxxpc.nora.models.CategoriaDeSonido
import com.emanoxxxpc.nora.models.Comando
import com.emanoxxxpc.nora.models.Usuario
import com.google.gson.JsonArray
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface NoraApiService {
    @POST("login/")
    suspend fun login(@Body usuario: Usuario): Response<Usuario>

    @POST("Usuarios/registrar")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>

    @POST("CategoriasDeSonido/")
    suspend fun createCategoriaDeSonido(@Header("Authorization") token: String, @Body categoriaDeSonido: CategoriaDeSonido): Response<CategoriaDeSonido>

    @POST("CategoriasDeSonido/{id}/Comando/")
    suspend fun addComando(@Header("Authorization") token: String,@Path("id") id: String, @Body comando: Comando): Response<CategoriaDeSonido>

    @GET("CategoriasDeSonido/search/{query}")
    suspend fun search(@Header("Authorization") token: String, @Path("query") query: String): Response<MutableList<CategoriaDeSonido>>

    @GET("CategoriasDeSonido/{id}")
    suspend fun getCategoriaByID(@Header("Authorization") token: String, @Path("id") id: String): Response<CategoriaDeSonido>

    @DELETE("CategoriasDeSonido/{id}/Archivo/{name}")
    suspend fun deleteArchivo(@Header("Authorization")token: String, @Path("id") id: String, @Path("name") name: String): Response<CategoriaDeSonido>

    @DELETE("CategoriasDeSonido/{id}")
    suspend fun deleteCategoria(@Header("Authorization")token: String, @Path("id") id: String): Response<CategoriaDeSonido>

    @DELETE("CategoriasDeSonido/{id}/Comando/{name}")
    suspend fun deleteComando(@Header("Authorization")token: String, @Path("id") id: String, @Path("name") name: String): Response<CategoriaDeSonido>

    companion object {
        fun getApiSession(host: String): NoraApiService {
            return Retrofit.Builder()
                .baseUrl("http://$host/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NoraApiService::class.java)
        }
    }
}