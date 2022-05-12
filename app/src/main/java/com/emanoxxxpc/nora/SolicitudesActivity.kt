package com.emanoxxxpc.nora

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.emanoxxxpc.nora.models.Usuario
import com.emanoxxxpc.nora.utils.Host
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class SolicitudesActivity : AppCompatActivity() {
    lateinit var rvSolicitudes: RecyclerView
    lateinit var toolbar:Toolbar
    lateinit var host:String
    private lateinit var token: String
    private lateinit var user: String
    private lateinit var authorization: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitudes)
        setSupportActionBar(findViewById(R.id.toolbar))
        rvSolicitudes = findViewById(R.id.rv_solicitudes)
        rvSolicitudes.layoutManager = LinearLayoutManager(this)
        toolbar=findViewById(R.id.toolbar)
        toolbar.title ="Usuarios"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val result = Host.verifyHost(getSharedPreferences("host", MODE_PRIVATE), this)
        if(result == null) {
            startActivity(Intent(this, SetHostActivity::class.java))
            finish()
        }
        host = result!!
        val prefe = getSharedPreferences("datos", MODE_PRIVATE)
        val usuario = prefe.getString("User", null)
        if (usuario == null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)

        } else {
            token = prefe.getString("Token", null)!!
            user = usuario
            authorization = "Bearer $token"
            loadUsuarios()
        }

    }
    fun loadUsuarios(){
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta = NoraApiService.getApiSession(host).getUsuarios(authorization)
            if (!respuesta.isSuccessful) {
                val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                runOnUiThread {
                    Toast.makeText(
                        this@SolicitudesActivity,
                        responseError.error,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                return@launch
            }
            var usuarios = respuesta.body()

            runOnUiThread {
                usuarios=(usuarios!!.filter {
                    return@filter it.username!=user
                }).toMutableList()
                rvSolicitudes.adapter =
                    RVSolicitudesAdapter(
                        host,authorization,usuarios!!,
                        this@SolicitudesActivity
                    )
            }

        }


    }
}