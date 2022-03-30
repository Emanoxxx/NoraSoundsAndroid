package com.emanoxxxpc.nora

import NoraAPI
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    lateinit var domain:String
    override fun onCreate(savedInstanceState: Bundle?) {
        domain=getString(R.string.url);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        var button:Button=findViewById(R.id.button);
        button.setOnClickListener{
            login()
        }
        var registro:Button=findViewById(R.id.registro);
        registro.setOnClickListener{
            val intent = Intent(this, Registro_Usuario::class.java)
            startActivity(intent);
        }
        supportActionBar!!.setDisplayShowTitleEnabled(false)

    }
    override fun onStart() {
        super.onStart();
        val prefe = getSharedPreferences("datos", MODE_PRIVATE)
        val usuario=prefe.getString("User",null)
        if(usuario!=null){
            val intent = Intent(this, Catalogo_Categorias::class.java)
            startActivity(intent);
        }
    }
    fun login(){
        val url = domain+"/login"
        var usuario:EditText=findViewById(R.id.userLogin)
        var pass:EditText=findViewById(R.id.pass_Login)
        val stringRequest: StringRequest = object : StringRequest( Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getString("Resultado")=="Success"){
                        if (jsonObject.getInt("isActive")==1){
                            Toast.makeText(this, "Hola "+jsonObject.getString("nombre")+"!!!", Toast.LENGTH_LONG).show()
                            saveSession(jsonObject.getString("token"),jsonObject.getString("username"),(jsonObject.getInt("isAdmin")==1))
                            val intent = Intent(this, Catalogo_Categorias::class.java)
                            startActivity(intent);
                        }else{
                            Toast.makeText(this, "Hola "+jsonObject.getString("nombre")+" tu cuenta no ha sido activada.", Toast.LENGTH_LONG).show()
                            usuario.setError("Usuario no activo");
                        }

                    }else{
                        usuario.setError("Revisa los datos");
                        pass.setError("Revisa los datos");
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                //Mensajes
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                //Change with your post params
                params["usr"] = usuario.getText().toString()
                params["psw"] = pass.getText().toString()
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
    fun loginRetrofit(){
        val retrofit=getRetrofit();
        val service =retrofit.create<NoraAPI>(NoraAPI::class.java)
    }
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(domain)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
    fun saveSession(token:String,usuario:String,isAdmin:Boolean) {
        val preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferencias.edit()
        editor.putString("User", usuario)
        editor.putString("Token", token)
        editor.putBoolean("IsAdmin", isAdmin)
        editor.commit()
        finish()
    }
}