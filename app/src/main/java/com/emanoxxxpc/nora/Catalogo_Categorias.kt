package com.emanoxxxpc.nora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu

class Catalogo_Categorias : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo_categorias)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.catalogo_menu, menu)
        return true
    }

}