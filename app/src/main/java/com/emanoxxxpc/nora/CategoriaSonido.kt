package com.emanoxxxpc.nora


import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.emanoxxxpc.nora.models.CategoriaDeSonido
import com.emanoxxxpc.nora.utils.Host
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*


class CategoriaSonido : AppCompatActivity() {
    private lateinit var host: String
    private lateinit var noraApi: NoraApiService
    lateinit var id:String;
    lateinit var tabs:TabLayout;
    lateinit var adapter: ViewPagerAdapter;
    lateinit var categoriaDeSonido: CategoriaDeSonido
    //var toolbar:Toolbar =findViewById(R.id.toolbar)
    var token: String="";
    var user: String="";
    var authorization: String="";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        setContentView(R.layout.activity_categoria_sonido)

        var id:String?=intent.getStringExtra("IDCategoria")
        // Get a support ActionBar corresponding to this toolbar and enable the Up button

        host=Host.verifyHost(getSharedPreferences("host", MODE_PRIVATE),this)!!

        noraApi = NoraApiService.getApiSession(host)
        CoroutineScope(Dispatchers.Main).launch {
            getCategoria(id!!,findViewById(R.id.toolbar))
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
                var aceptarDialog: AlertDialog =AlertDialog.Builder(this).apply {
                    setPositiveButton(R.string.accept,
                        DialogInterface.OnClickListener { dialog, id ->
                            deleteCategoria()
                        })
                    setNegativeButton(R.string.cancel,
                        DialogInterface.OnClickListener { dialog, id ->
                        })
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

    suspend fun getCategoria(id: String, toolbar: Toolbar){
            val respuesta = noraApi.getCategoriaByID(authorization, id)
            if (!respuesta.isSuccessful) {
                val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                runOnUiThread {
                    Toast.makeText(
                        this@CategoriaSonido,
                        responseError.error,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                println(responseError)

            }

        categoriaDeSonido= respuesta.body()!!
        toolbar.title=categoriaDeSonido.nombre
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tabs=findViewById(R.id.tabCategoria)
        adapter = ViewPagerAdapter(this@CategoriaSonido, categoriaDeSonido,host,authorization)
        var pager=findViewById<ViewPager2>(R.id.pager);
        pager.adapter=adapter
    }
    fun deleteCategoria(){
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta = noraApi.deleteCategoria("Bearer $token",categoriaDeSonido.id!!)
            if (!respuesta.isSuccessful) {
                val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                runOnUiThread {
                    Toast.makeText(this@CategoriaSonido, "No pude completar la operacion", Toast.LENGTH_SHORT).show()
                }

                return@launch
            }
            val categoriaDeSonido:CategoriaDeSonido = respuesta.body()!!

            runOnUiThread {
                finish()
            }


        }
    }
}