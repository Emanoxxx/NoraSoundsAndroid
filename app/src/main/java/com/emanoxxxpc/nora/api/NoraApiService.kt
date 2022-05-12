package com.emanoxxxpc.nora.api

import com.emanoxxxpc.nora.models.CategoriaDeSonido
import com.emanoxxxpc.nora.models.Comando
import com.emanoxxxpc.nora.models.Mensaje
import com.emanoxxxpc.nora.models.Usuario
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface NoraApiService {
    @POST("login/")
    suspend fun login(@Body usuario: Usuario): Response<Usuario>

    @POST("Usuarios/registrar")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>

    @PUT("Usuarios/{username}/activar/")
    suspend fun activarUsuario(@Header("Authorization") token: String,@Path("username") username: String): Response<Usuario>

    @PUT("Usuarios/{username}/desactivar/")
    suspend fun desactivarUsuario(@Header("Authorization") token: String,@Path("username") username: String): Response<Usuario>

    @PUT("Usuarios/{username}/toAdmin/")
    suspend fun toadmin(@Header("Authorization") token: String,@Path("username") username: String): Response<Usuario>

    @PUT("Usuarios/{username}/toUser/")
    suspend fun toUser(@Header("Authorization") token: String,@Path("username") username: String): Response<Usuario>

    @GET("Usuarios/")
    suspend fun getUsuarios(@Header("Authorization") token: String): Response<MutableList<Usuario>>

    @POST("CategoriasDeSonido/")
    suspend fun createCategoriaDeSonido(@Header("Authorization") token: String, @Body categoriaDeSonido: CategoriaDeSonido): Response<CategoriaDeSonido>

    @POST("CategoriasDeSonido/{id}/Comando/")
    suspend fun addComando(@Header("Authorization") token: String,@Path("id") id: String, @Body comando: Comando): Response<CategoriaDeSonido>

    @PUT("CategoriasDeSonido/{id}/Comando/{comandoActual}")
    suspend fun editarComando(@Header("Authorization") token: String, @Path("id") id: String, @Path("comandoActual") comandoActual: String, @Body comando: Comando): Response<CategoriaDeSonido>

    @GET("/")
    suspend fun revisarDisponibilidad(): Response<Mensaje>

    @Multipart @POST("CategoriasDeSonido/{id}/Archivo/")
    suspend fun addArchivo(@Header("Authorization") token: String,@Path("id") id: String, @Part audio: MultipartBody.Part): Response<CategoriaDeSonido>

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
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
                .baseUrl("http://$host/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(NoraApiService::class.java)
        }
    }
}