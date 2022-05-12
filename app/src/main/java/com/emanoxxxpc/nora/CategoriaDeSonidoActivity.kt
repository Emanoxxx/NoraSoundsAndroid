package com.emanoxxxpc.nora


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.emanoxxxpc.nora.models.CategoriaDeSonido
import com.emanoxxxpc.nora.utils.Host
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CategoriaDeSonidoActivity : AppCompatActivity() {
    private lateinit var host: String
    private lateinit var noraApi: NoraApiService
    lateinit var id: String
    private lateinit var tabs: TabLayout
    private lateinit var adapter: ViewPagerAdapter
    lateinit var categoriaDeSonido: CategoriaDeSonido

    var token: String = ""
    private var user: String = ""
    private var authorization: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        }
        setContentView(R.layout.activity_categoria_sonido)

        val id: String? = intent.getStringExtra("IDCategoria")
        // Get a support ActionBar corresponding to this toolbar and enable the Up button

        host = Host.verifyHost(getSharedPreferences("host", MODE_PRIVATE), this)!!

        noraApi = NoraApiService.getApiSession(host)

        CoroutineScope(Dispatchers.Main).launch {
            getCategoria(id!!, findViewById(R.id.toolbar))
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.categoriasonidomenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_trash -> {
                val aceptarDialog: AlertDialog = AlertDialog.Builder(this).apply {
                    setPositiveButton(
                        R.string.accept
                    ) { _, _ ->
                        deleteCategoria()
                    }
                    setNegativeButton(
                        R.string.cancel
                    ) { _, _ ->
                    }
                }.create()
                aceptarDialog.setCancelable(true)
                aceptarDialog.setTitle("Borrar Categoria ")
                aceptarDialog.setMessage("¿Seguro que desea borrar ${categoriaDeSonido.nombre}?")
                aceptarDialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private suspend fun getCategoria(id: String, toolbar: Toolbar) {
        val respuesta = noraApi.getCategoriaByID(authorization, id)
        if (!respuesta.isSuccessful) {
            val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
            runOnUiThread {
                Toast.makeText(
                    this@CategoriaDeSonidoActivity,
                    responseError.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            println(responseError)

        }

        categoriaDeSonido = respuesta.body()!!
        toolbar.title = categoriaDeSonido.nombre
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tabs = findViewById(R.id.tabCategoria)
        adapter =
            ViewPagerAdapter(this@CategoriaDeSonidoActivity, categoriaDeSonido, host, authorization)
        val pager = findViewById<ViewPager2>(R.id.pager)
        pager.adapter = adapter

        val tabLayoutMediator=TabLayoutMediator(tabs,pager,TabLayoutMediator.TabConfigurationStrategy{tab, position ->
            when (position){
                0->{
                    tab.text="Comandos"
                    tab.setIcon(R.drawable.ic_action_sort_1)
                }
                1->{
                    tab.text="Archivos"
                    tab.setIcon(R.drawable.ic_headset)
                }
            }
        })
        tabLayoutMediator.attach()
    }

    private fun deleteCategoria() {
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta = noraApi.deleteCategoria("Bearer $token", categoriaDeSonido.id!!)
            if (!respuesta.isSuccessful) {
                ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                runOnUiThread {
                    Toast.makeText(
                        this@CategoriaDeSonidoActivity,
                        "No pude completar la operacion",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                return@launch
            }
            runOnUiThread {
                finish()
            }


        }
    }
}