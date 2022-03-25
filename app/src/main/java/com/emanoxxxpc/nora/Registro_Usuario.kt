package com.emanoxxxpc.nora

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Registro_Usuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)
        setSupportActionBar(findViewById(R.id.toolbar))
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        var registro: Button =findViewById(R.id.registro_button);
        registro.setOnClickListener{

        }
        var volverLogin: Button =findViewById(R.id.volver_Login);
        volverLogin.setOnClickListener{
            onBackPressed();
        }
    }
}