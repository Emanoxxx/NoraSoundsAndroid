package com.emanoxxxpc.nora.api

import com.emanoxxxpc.nora.models.CategoriaDeSonido
import com.emanoxxxpc.nora.models.Usuario
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface NoraApiService {
    @POST("login/")
    suspend fun login(@Body user: Usuario): Response<Usuario>

    @POST("CategoriasDeSonido/")
    suspend fun createCategoriaDeSonido(@Header("Authorization") token: String, @Body categoriaDeSonido: CategoriaDeSonido): Response<CategoriaDeSonido>

    @GET("CategoriasDeSonido/search/{query}")
    suspend fun search(@Header("Authorization") token: String, @Path("query") query: String): Response<MutableList<CategoriaDeSonido>>


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