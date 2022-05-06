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

class RV_Comandos_Adapter (var comandos :JSONArray):
    RecyclerView.Adapter<RV_Comandos_Adapter.ViewHolder>(){

    inner class ViewHolder(itemView: View ): RecyclerView.ViewHolder(itemView){
        val itemNombre:TextView=itemView.findViewById(R.id.cv_nombre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ViewHolder {
        println("llegue");
        val v=LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemNombre.text=comandos.getString(position)

    }

    override fun getItemCount(): Int {
        return comandos.length()
    }

}