package com.emanoxxxpc.nora.api

import com.emanoxxxpc.nora.models.Usuario
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioApi {
    @POST("/login")
    fun login(@Body loginData: LoginData): Call<Usuario>
}

data class LoginData(
    @SerializedName("usr") val username: String,
    @SerializedName("psw")val password: String,

    )