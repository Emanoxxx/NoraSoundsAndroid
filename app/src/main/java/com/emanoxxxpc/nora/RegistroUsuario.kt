package com.emanoxxxpc.nora

import android.content.Intent
import android.os.Bundle
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

class RegistroUsuario : AppCompatActivity() {
    private lateinit var etNombre: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordConfirm: EditText
    lateinit var host: String
    private lateinit var noraApi: NoraApiService

    override fun onStart() {
        super.onStart()
        host = Host.verifyHost(getSharedPreferences("host", MODE_PRIVATE), this)!!
        noraApi = NoraApiService.getApiSession(host)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)
        setSupportActionBar(findViewById(R.id.toolbar))
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)


        val btnRegistrar: Button = findViewById(R.id.registro_button)
        btnRegistrar.setOnClickListener {
            registrar()
        }
        val regresar: Button = findViewById(R.id.volver_Login)
        regresar.setOnClickListener {
            onBackPressed()
        }
        etNombre = findViewById(R.id.et_nombre)
        etEmail = findViewById(R.id.et_email)
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        etPasswordConfirm = findViewById(R.id.et_password_confirm)
    }

    private fun registrar() {
        etNombre.error = null
        etUsername.error = null
        etEmail.error = null
        etPassword.error = null
        etPasswordConfirm.error = null

        val nombre = etNombre.text.toString()
        val username = etUsername.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val passwordConfirm = etPasswordConfirm.text.toString()

        if (nombre == "") {
            etNombre.error = "Ingrese su nombre"
            return
        }
        if (username == "") {
            etUsername.error = "Ingrese su nombre de usuario"
            return
        }
        if (email == "") {
            etEmail.error = "Ingrese su correo"
            return
        }
        if (password == "") {
            etPassword.error = "Ingrese su contrase??a"
            return
        }
        if (password.length < 6) {
            etPassword.error = "Ingrese al menos 6 caracteres"
            return
        }
        if (password != passwordConfirm) {
            etPassword.error = "Las contrase??as no coinciden"
            return
        }

        val usuario = Usuario(username, password, nombre, email)

        CoroutineScope(Dispatchers.IO).launch {
            val respuesta = noraApi.registrarUsuario(usuario)
            if (!respuesta.isSuccessful) {
                val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                runOnUiThread {
                    if (responseError.code == "P2002") {
                        Toast.makeText(
                            this@RegistroUsuario,
                            "El usuario o correo ya se encuentran registrados",
                            Toast.LENGTH_SHORT
                        ).show()
                        etUsername.error = "Intente con otro nombre nombre de usuario"
                        etEmail.error = "Intente con otro correo"
                    }

                }
                return@launch
            }

            runOnUiThread {
                Toast.makeText(
                    this@RegistroUsuario,
                    "Gracias por registrarte ${usuario.nombre}, tu solicitud ha sido enviada y un administrador pronto activar?? tu cuenta.",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@RegistroUsuario, MainActivity::class.java))
            }
        }


    }

}