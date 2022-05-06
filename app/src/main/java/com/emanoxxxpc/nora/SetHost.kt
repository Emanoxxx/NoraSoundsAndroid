package com.emanoxxxpc.nora

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.emanoxxxpc.nora.api.NoraApiService

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
            saveHost(findViewById<EditText>(R.id.et_nombre).text.toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        if (host != null) {
            findViewById<EditText>(R.id.et_nombre).setText(host)
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