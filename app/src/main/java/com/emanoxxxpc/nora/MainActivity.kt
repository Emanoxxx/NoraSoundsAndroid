package com.emanoxxxpc.nora

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.emanoxxxpc.nora.models.Usuario
import com.emanoxxxpc.nora.utils.Host
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var noraApi: NoraApiService
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var host: String

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
            val intent = Intent(this, RegistroUsuario::class.java)
            startActivity(intent)
        }

        supportActionBar!!.setDisplayShowTitleEnabled(false)


        etUsername = findViewById(R.id.userLogin)
        etPassword = findViewById(R.id.et_password)
    }

    override fun onStart() {
        super.onStart()
        val result = Host.verifyHost(getSharedPreferences("host", MODE_PRIVATE), this)
        if(result == null) {
            startActivity(Intent(this, SetHostActivity::class.java))
            finish()
        }
        host = result!!
        noraApi = NoraApiService.getApiSession(host)
        val preferencias = getSharedPreferences("datos", MODE_PRIVATE)
        val usuario = preferencias.getString("User", null)
        if (usuario != null) {
            val intent = Intent(this, CatalogoCategoriasActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {

        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        etUsername.error = null
        etPassword.error = null

        if (username == "") {
            etUsername.error = "Ingresa tu usuario."
            return
        }

        if (password == "") {
            etPassword.error = "Ingresa tu contrase??a."
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
                startActivity(Intent(this@MainActivity, CatalogoCategoriasActivity::class.java))
            }

        }
    }

    private fun saveSession(token: String, usuario: String, isAdmin: Boolean) {
        val preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferencias.edit()
        editor.putString("User", usuario)
        editor.putString("Token", token)
        editor.putBoolean("IsAdmin", isAdmin)
        editor.apply()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.buttonCambiarHost -> {
                Host.changeHost(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
}