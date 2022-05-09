package com.emanoxxxpc.nora

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.emanoxxxpc.nora.api.NoraApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class SetHostActivity : AppCompatActivity() {
    private lateinit var noraApi: NoraApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_host)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onStart() {
        super.onStart()
        val preferencias = getSharedPreferences("host", MODE_PRIVATE)
        val host = preferencias.getString("Host", null)
        val etHost = findViewById<EditText>(R.id.et_host)
        findViewById<Button>(R.id.volver_Host).setOnClickListener {
            onBackPressed()
        }
        findViewById<Button>(R.id.establecer_button).setOnClickListener {
            val hostname = etHost.text.toString()
            Toast.makeText(
                this@SetHostActivity,
                "Revisando disponibilidad del servicio...",
                Toast.LENGTH_SHORT
            ).show()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    noraApi = NoraApiService.getApiSession(hostname)
                    val respuesta = noraApi.revisarDisponibilidad()
                    if (respuesta.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(
                                this@SetHostActivity,
                                "Host actualizado",
                                Toast.LENGTH_SHORT
                            ).show()
                            saveHost(hostname)
                            val intent =
                                Intent(this@SetHostActivity, CatalogoCategoriasActivity::class.java)
                            startActivity(intent)
                            this@SetHostActivity.finish()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@SetHostActivity,
                                "Parece que hay un problema con el host que ingresaste",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (excepcion: Exception) {
                    runOnUiThread {
                        etHost.error = "Hay un problema con el host ingresado"
                        Toast.makeText(
                            this@SetHostActivity,
                            "Parece que hay un problema con el host que ingresaste",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }


            }

        }
        if (host != null) {
            findViewById<EditText>(R.id.et_host).setText(host)
        } else {
            findViewById<Button>(R.id.volver_Host).isVisible = false
        }
    }

    private fun saveHost(host: String) {
        val preferencias = getSharedPreferences("host", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferencias.edit()
        editor.putString("Host", host)
        editor.apply()
        finish()
    }
}