package com.emanoxxxpc.nora


import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.emanoxxxpc.nora.utils.Host
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Catalogo_Categorias  : AppCompatActivity(), DialogInterface.OnDismissListener {
    private var nombresSonido = mutableListOf<String>()
    private lateinit var rv_sonidos: RecyclerView
    private lateinit var token: String;
    private lateinit var user: String;
    private lateinit var host: String
    private lateinit var authorization: String
    private lateinit var noraApi: NoraApiService
    private lateinit var fabAddCategoriaDeSonidoDialog: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo_categorias)
        setSupportActionBar(findViewById(R.id.toolbar))
        rv_sonidos = findViewById(R.id.rv_sonidos)
        rv_sonidos.layoutManager = LinearLayoutManager(this)
        fabAddCategoriaDeSonidoDialog = findViewById(R.id.fab_add_categoria_de_sonido)

        fabAddCategoriaDeSonidoDialog.setOnClickListener {
            var addCategoriaDeSonidoDialog = AddCategoriaDeSonidoDialog.newInstance(host, authorization)
            addCategoriaDeSonidoDialog.show(supportFragmentManager, AddCategoriaDeSonidoDialog.TAG)
        }
    }

    override fun onStart() {
        super.onStart()
        host =Host.verifyHost(getSharedPreferences("host", MODE_PRIVATE),this)!!
        noraApi = NoraApiService.getApiSession(host)
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
            search("")
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
            R.id.buttonCambiarHost -> {
                Host.changeHost(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.catalogo_menu, menu)
        val item: MenuItem = menu.findItem(R.id.buttonSolicitudes);
        val prefe = getSharedPreferences("datos", MODE_PRIVATE)
        val isAdmin = prefe.getBoolean("IsAdmin", false)
        if (isAdmin) {
            item.isVisible = true
        } else {
            item.isVisible = false
        }

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    clearFocus()
                    search(p0.toString())
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    search(p0.toString())
                    return false
                }

            })
            queryHint = getString(R.string.search_hint)
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

    fun search(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta = noraApi.search(authorization, query)
            if (!respuesta.isSuccessful) {
                val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                runOnUiThread {
                    Toast.makeText(
                        this@Catalogo_Categorias,
                        responseError.error,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                return@launch
            }
            val categoriasDeSonido = respuesta.body()

            runOnUiThread {
                rv_sonidos.adapter = RV_Sonido_Adapter(categoriasDeSonido!!, this@Catalogo_Categorias,host)
            }

        }
    }

    override fun onDismiss(p0: DialogInterface?) {
        Toast.makeText(this, "Dismiss", Toast.LENGTH_SHORT).show()
    }
}
