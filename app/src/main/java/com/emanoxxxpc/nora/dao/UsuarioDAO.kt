package com.emanoxxxpc.nora.dao

import com.emanoxxxpc.nora.api.LoginData
import com.emanoxxxpc.nora.api.NoraApi
import com.emanoxxxpc.nora.api.UsuarioApi
import com.emanoxxxpc.nora.models.Usuario
import retrofit2.Call
import retrofit2.await
import java.lang.Exception

class UsuarioDAO {
    companion object {
        private val apiSession = NoraApi.getApiSession()
        private val usuarioApi = apiSession.create(UsuarioApi::class.java)

        suspend fun login(username: String, password: String): Usuario? {

            val loginData = LoginData(username, password)

            var usuario: Usuario? = null
            try {
                val call: Call<Usuario> = usuarioApi.login(loginData)
                usuario = call.await()
            } catch (exception: Exception) {

            }
            return usuario
        }
    }
}