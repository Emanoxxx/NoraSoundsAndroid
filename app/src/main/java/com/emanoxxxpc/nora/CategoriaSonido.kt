package com.emanoxxxpc.nora


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.emanoxxxpc.nora.utils.Host
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject


class CategoriaSonido : AppCompatActivity() {
    lateinit var categoria:JSONObject;
    lateinit var archivos:JSONArray;
    lateinit var comandos:JSONArray;
    lateinit var id:String;
    lateinit var tabs:TabLayout;
    lateinit var adapter: ViewPagerAdapter;
    var token: String="";
    var user: String="";
    var authorization: String="";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoria_sonido)
        var toolbar: Toolbar =findViewById(R.id.toolbar)
        var cadena:String?=intent.getStringExtra("CategoriaSonido")
        categoria = JSONObject(cadena);
        toolbar.title=categoria.getString("nombre")
        id=categoria.getString("id")

        archivos= JSONArray(categoria.getString("archivos"))
        comandos=JSONArray(categoria.getString("comandos"))
        var host: String=""
        host=Host.verifyHost(getSharedPreferences("host", MODE_PRIVATE),this)!!
        setSupportActionBar(toolbar)
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tabs=findViewById(R.id.tabCategoria)

        val prefe = getSharedPreferences("datos", MODE_PRIVATE)
        val usuario = prefe.getString("User", null)
        if (usuario == null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else {
            token = prefe.getString("Token", null)!!
            user = usuario
            authorization = "Bearer $token"
        }
        adapter = ViewPagerAdapter(this,categoria,host,authorization)
        var pager=findViewById<ViewPager2>(R.id.pager);
        pager.adapter=adapter

    }

    override fun onStart() {
        super.onStart()


    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.categoriasonidomenu, menu)
        return true
    }

}