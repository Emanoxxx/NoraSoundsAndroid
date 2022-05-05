package com.emanoxxxpc.nora

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.emanoxxxpc.nora.databinding.ActivitySetHostBinding
import com.emanoxxxpc.nora.models.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetHost : AppCompatActivity() {
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
        findViewById<Button>(R.id.volver_Host).setOnClickListener{
            onBackPressed()
        }
        findViewById<Button>(R.id.establecer_button).setOnClickListener{
            saveHost(findViewById<EditText>(R.id.nombre_Host).text.toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        if (host != null) {
            findViewById<EditText>(R.id.nombre_Host).setText(host)
        }else{
            findViewById<Button>(R.id.volver_Host).isVisible=false;
        }
    }
    fun saveHost(host: String) {
        val preferencias = getSharedPreferences("host", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferencias.edit()
        editor.putString("Host", host)
        editor.apply()
        finish()
    }
}