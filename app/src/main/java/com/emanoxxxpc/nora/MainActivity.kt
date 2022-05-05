package com.emanoxxxpc.nora

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.emanoxxxpc.nora.models.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var noraApi: NoraApiService
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val loginButton: Button = findViewById(R.id.button)
        loginButton.setOnClickListener {
            login()
        }

        val registroButton: Button = findViewById(R.id.registro)
        registroButton.setOnClickListener {
            val intent = Intent(this, Registro_Usuario::class.java)
            startActivity(intent)
        }

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        noraApi = NoraApiService.getApiSession()


        etUsername = findViewById(R.id.userLogin)
        etPassword = findViewById(R.id.pass_Login)
    }

    override fun onStart() {
        super.onStart()
        val preferencias = getSharedPreferences("datos", MODE_PRIVATE)
        val usuario = preferencias.getString("User", null)
        if (usuario != null) {
            val intent = Intent(this, Catalogo_Categorias::class.java)
            startActivity(intent)
        }
    }

    fun login() {

        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        etUsername.error = null
        etPassword.error = null

        if (username == "") {
            etUsername.error = "Ingresa tu usuario."
            return
        }

        if (password == "") {
            etPassword.error = "Ingresa tu contraseña."
            return
        }

        val user = Usuario(username, password)
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta = noraApi.login(user)

            if (!respuesta.isSuccessful) {
                val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                runOnUiThread {
                    Toast.makeText(this@MainActivity, responseError.error, Toast.LENGTH_SHORT)
                        .show()
                }
                return@launch
            }

            val usuario: Usuario? = respuesta.body()

            runOnUiThread {
                if (!usuario!!.isActive!!) {
                    Toast.makeText(
                        this@MainActivity,
                        "Lo siento ${usuario.nombre}, su cuenta no se encuentra activa. :(",
                        Toast.LENGTH_LONG
                    ).show()
                    etUsername.error = "Cuenta inactiva"
                    return@runOnUiThread
                }
                Toast.makeText(
                    this@MainActivity,
                    "Bienvenide ${usuario.nombre}",
                    Toast.LENGTH_SHORT
                ).show()

                saveSession(usuario.token!!, usuario.username, usuario.isAdmin!!)
                startActivity(Intent(this@MainActivity, Catalogo_Categorias::class.java))
            }

        }
    }

    fun saveSession(token: String, usuario: String, isAdmin: Boolean) {
        val preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferencias.edit()
        editor.putString("User", usuario)
        editor.putString("Token", token)
        editor.putBoolean("IsAdmin", isAdmin)
        editor.apply()
        finish()
    }
}