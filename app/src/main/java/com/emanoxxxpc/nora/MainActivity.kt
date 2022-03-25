package com.emanoxxxpc.nora

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Request.Method.POST
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        var button:Button=findViewById(R.id.button);
        button.setOnClickListener{
            login()
        }
        var registro:Button=findViewById(R.id.registro);
        registro.setOnClickListener{
            val intent = Intent(this, Catalogo_Categorias::class.java)
            startActivity(intent);
        }
        supportActionBar!!.setDisplayShowTitleEnabled(false)

    }
    fun login(){
        val url = "http://emanoxxx.com:8080/login"
        val stringRequest: StringRequest = object : StringRequest( Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                try {
                    val jsonObject = JSONObject(response)
                    //Parse your api responce here
                    /*val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)*/
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                //Change with your post params
                params["usr"] = findViewById<EditText>(R.id.userLogin).toString()
                params["psw"] = findViewById<EditText>(R.id.passwordLogin).toString()
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}