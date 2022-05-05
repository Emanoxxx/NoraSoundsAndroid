package com.emanoxxxpc.nora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.emanoxxxpc.nora.utils.Host
import org.json.JSONException
import org.json.JSONObject

class Registro_Usuario : AppCompatActivity() {
    lateinit var domain:String
    override fun onStart() {
        super.onStart()
        Host.verifyHost(getSharedPreferences("host", MODE_PRIVATE),this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        domain=getString(R.string.url);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)
        setSupportActionBar(findViewById(R.id.toolbar))
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        var registro: Button =findViewById(R.id.registro_button);
        registro.setOnClickListener{
            verify()
        }
        var volverLogin: Button =findViewById(R.id.volver_Login);
        volverLogin.setOnClickListener{
            onBackPressed();
        }
    }
    fun verify(){
        var nombre:EditText=findViewById(R.id.nombre_Host)
        var usuario:EditText=findViewById(R.id.user_res)
        var correo:EditText=findViewById(R.id.correo_res)
        var pass1:EditText=findViewById(R.id.pass_Login)
        var pass2:EditText=findViewById(R.id.pass_res_2)
        nombre.error=null;
        usuario.error=null;
        correo.error=null;
        pass2.error=null;
        pass1.error=null;
        if (nombre.text.toString().equals("")){
            return nombre.setError("Este campo no puede estar vacio");
        }
        if (usuario.text.toString().equals("")){
            return usuario.setError("Este campo no puede estar vacio");
        }
        if (correo.text.toString().equals("")){
            return correo.setError("Este campo no puede estar vacio");
        }
        if (pass1.text.toString().equals("")){
            return pass1.setError("Este campo no puede estar vacio");
        }
        if (pass2.text.toString().equals("")){
            return pass2.setError("Este campo no puede estar vacio");
        }
        var p1:String=pass1.text.toString()
        var p2:String=pass2.text.toString()
        if(!p1.equals(p2)){
            pass1.setError("Contraseña no coincide.");
            return pass2.setError("Contraseña no coincide.");
        }
        if (p1.length<4){
            pass1.setError("Al menos 4 caracteres");
            return pass2.setError("Al menos 4 caracteres");
        }
        var name:String=nombre.text.toString()
        var user:String=usuario.text.toString()
        var email:String=correo.text.toString()
        register(name,user,email,p1)
        onBackPressed();


    }
    fun register(nombre:String,usuario:String,correo:String,pass:String){
        val url = domain+"/register"
        val stringRequest: StringRequest = object : StringRequest( Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getString("Resultado")=="Success"){
                        Toast.makeText(this, "Solicitud enviada, espera tu confirmacion!!!", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "Upss!!! Algo salio mal", Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Upss!!! Algo salio mal, Revisa los datos o la conexion", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                //Change with your post params
                params["nombre"] =nombre
                params["username"] =usuario
                params["email"] =correo
                params["password"] =pass
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}