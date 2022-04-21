package com.emanoxxxpc.nora

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import org.json.JSONObject
import androidx.recyclerview.widget.RecyclerView

class RV_Sonido_Adapter (private var sonidos:List<String>,var activity: Activity):
    RecyclerView.Adapter<RV_Sonido_Adapter.ViewHolder>(){
        inner class ViewHolder(itemView: View ,activity: Activity): RecyclerView.ViewHolder(itemView){
            val itemNombre:TextView=itemView.findViewById(R.id.cv_nombre)
            init {
                itemView.setOnClickListener{v:View->
                    val position: Int=adapterPosition
                    val intent = Intent(activity, CategoriaSonido::class.java).apply {
                        putExtra("CategoriaSonido", itemNombre.text)
                    }
                    startActivity(activity,intent, Bundle.EMPTY)

                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.card_categoria_sonido,parent,false)
        return ViewHolder(v, activity)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemNombre.text=sonidos[position]
    }

    override fun getItemCount(): Int {
        return sonidos.size
    }

}
