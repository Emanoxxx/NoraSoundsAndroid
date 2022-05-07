package com.emanoxxxpc.nora

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class RV_Comandos_Adapter (var comandos :JSONArray):
    RecyclerView.Adapter<RV_Comandos_Adapter.ViewHolder>(){

    inner class ViewHolder(itemView: View ): RecyclerView.ViewHolder(itemView){
        val itemNombre:TextView=itemView.findViewById(R.id.cv_nombre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ViewHolder {
        val v=LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_comando_item,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemNombre.text=comandos.getString(position)

    }

    override fun getItemCount(): Int {
        return comandos.length()
    }

}