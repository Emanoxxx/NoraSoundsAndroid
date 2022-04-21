package com.emanoxxxpc.nora


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class CategoriaSonido : AppCompatActivity() {
    lateinit var categoria:String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoria_sonido)
        var toolbar: Toolbar =findViewById(R.id.toolbar)
        categoria = intent.getStringExtra("CategoriaSonido").toString()
        toolbar.title=categoria
        setSupportActionBar(toolbar)
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.categoriasonidomenu, menu)
        return true
    }

}