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
import androidx.core.content.ContextCompat.startActivity
import org.json.JSONObject
import androidx.recyclerview.widget.RecyclerView
import com.emanoxxxpc.nora.models.CategoriaDeSonido
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

class RV_Sonido_Adapter (private var sonidos:List<CategoriaDeSonido>, var activity: Activity, var host:String):
    RecyclerView.Adapter<RV_Sonido_Adapter.ViewHolder>(){
        inner class ViewHolder(itemView: View ,activity: Activity): RecyclerView.ViewHolder(itemView){
            val itemNombre:TextView=itemView.findViewById(R.id.cv_nombre)
            var itemCategoria:CategoriaDeSonido?=null
            val playbutton:FloatingActionButton=itemView.findViewById(R.id.reproducir);
            init {
                itemView.setOnClickListener{v:View->
                    val position: Int=adapterPosition
                    val intent = Intent(activity, CategoriaSonido::class.java).apply {
                        putExtra("CategoriaSonido", "{" +
                                "nombre:'"+sonidos[position].nombre+"'," +
                                "id:"+sonidos[position].id+"," +
                                "archivos:"+ JSONArray(sonidos[position].archivos).toString()+"," +
                                "comandos:"+JSONArray(sonidos[position].comandos).toString() +
                                "}")
                    }
                    startActivity(activity,intent, Bundle.EMPTY)
                }


                playbutton.setOnClickListener{v:View->
                    if (!CategoriaDeSonido.playSound(itemCategoria!!.archivos!!,itemCategoria!!.id!!,host)){
                        Toast.makeText(activity, "No encontre archivo a reproducir", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.card_categoria_sonido,parent,false)
        return ViewHolder(v, activity)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemNombre.text=sonidos[position].nombre
        holder.itemCategoria=sonidos[position]
        if(sonidos[position].archivos != null) {
            holder.playbutton.isEnabled = !sonidos[position].archivos!!.isEmpty()
        }

    }

    override fun getItemCount(): Int {
        return sonidos.size
    }

}
