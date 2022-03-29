package com.emanoxxxpc.nora


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class Catalogo_Categorias : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo_categorias)
        setSupportActionBar(findViewById(R.id.toolbar))


    }

    override fun onStart() {
        super.onStart()
        val prefe = getSharedPreferences("datos", MODE_PRIVATE)
        val usuario=prefe.getString("User",null)
        if(usuario==null){
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.buttonSolicitudes -> {
                true
            }
            R.id.buttonCerrarSesion -> {
                closeSession()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.catalogo_menu, menu)
        val item:MenuItem = menu.findItem(R.id.buttonSolicitudes);
        val prefe = getSharedPreferences("datos", MODE_PRIVATE)
        val isAdmin=prefe.getBoolean("IsAdmin",false)
        if (isAdmin){
            item.isVisible=true
        }else{
            item.isVisible=false
        }
        return true
    }
    fun closeSession() {
        val preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferencias.edit()
        editor.remove("User")
        editor.remove("Token")
        editor.remove("IsAdmin")
        editor.commit()
        finish()
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}