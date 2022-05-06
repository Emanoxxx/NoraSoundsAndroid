package com.emanoxxxpc.nora


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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoria_sonido)
        var toolbar: Toolbar =findViewById(R.id.toolbar)
        var cadena:String?=intent.getStringExtra("CategoriaSonido")
        categoria = JSONObject(cadena);

        println(cadena)
        toolbar.title=categoria.getString("nombre")
        id=categoria.getString("id")

        archivos= JSONArray(categoria.getString("archivos"))
        comandos=JSONArray(categoria.getString("comandos"))


        setSupportActionBar(toolbar)
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tabs=findViewById(R.id.tabCategoria)
        adapter = ViewPagerAdapter(this,categoria)
        var pager=findViewById<ViewPager2>(R.id.pager);
        pager.adapter=adapter

    }

    override fun onStart() {
        super.onStart()
        Host.verifyHost(getSharedPreferences("host", MODE_PRIVATE),this)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.categoriasonidomenu, menu)
        return true
    }

}