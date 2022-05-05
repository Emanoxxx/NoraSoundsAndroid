package com.emanoxxxpc.nora.api

import com.emanoxxxpc.nora.models.CategoriaDeSonido
import com.emanoxxxpc.nora.models.Usuario
import retrofit2.Response
import retrofit2.http.*

interface NoraApiService {
    @POST("login/")
    suspend fun login(@Body user: Usuario): Response<Usuario>

    @GET("CategoriasDeSonido/search/{query}")
    suspend fun search(@Header("Authorization") token: String, @Path("query") query: String): Response<MutableList<CategoriaDeSonido>>
}