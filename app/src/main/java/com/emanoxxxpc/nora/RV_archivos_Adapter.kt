package com.emanoxxxpc.nora

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import org.json.JSONObject
import androidx.recyclerview.widget.RecyclerView
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.emanoxxxpc.nora.models.CategoriaDeSonido
import com.emanoxxxpc.nora.utils.Host
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class RV_archivos_Adapter (var categoria: CategoriaDeSonido, var host: String, var token: String):
    RecyclerView.Adapter<RV_archivos_Adapter.ViewHolder>(){

    inner class ViewHolder(itemView: View ): RecyclerView.ViewHolder(itemView){
        val itemNombre:TextView=itemView.findViewById(R.id.cv_nombre)
        val playbutton:FloatingActionButton=itemView.findViewById(R.id.RV_archivo_reproducir);
        val deleteButton:FloatingActionButton=itemView.findViewById(R.id.RV_archivo_borrar_button);
        lateinit var noraApi: NoraApiService
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ViewHolder {
        val v=LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_archivo_item,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemNombre.text=categoria.archivos!![position]
        var  archivo:MutableList<String> = mutableListOf()
        archivo.add(categoria.archivos!![position])
        holder.playbutton.setOnClickListener{v:View->
            CategoriaDeSonido.playSound(archivo,categoria!!.id!!,host)
        }
        holder.deleteButton.setOnClickListener{v:View->
            holder.noraApi = NoraApiService.getApiSession(host)
            CoroutineScope(Dispatchers.IO).launch {
                holder.noraApi.deleteArchivo(token,categoria!!.id!!, categoria.archivos!![position])
            }
        }
    }

    override fun getItemCount(): Int {
        return categoria.archivos!!.size
    }
    /*fun deleteArchivo(context: Activity, noraApi: NoraApiService, id: String, name:String, authorization: String):CategoriaDeSonido {
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta = noraApi.deleteArchivo(authorization,id,name)
            if (!respuesta.isSuccessful) {
                val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                runOnUiThread {
                    Toast.makeText(
                        context,
                        responseError.error,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                return@launch
            }
            val categoriaDeSonido:CategoriaDeSonido = respuesta.body()!!

            runOnUiThread {
                return@launch categoriaDeSonido
            }

        }
    }*/

}