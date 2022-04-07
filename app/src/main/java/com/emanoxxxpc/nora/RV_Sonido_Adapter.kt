package com.emanoxxxpc.nora

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import androidx.recyclerview.widget.RecyclerView

class RV_Sonido_Adapter (private var sonidos:List<String>):
    RecyclerView.Adapter<RV_Sonido_Adapter.ViewHolder>(){
        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val itemNombre:TextView=itemView.findViewById(R.id.cv_nombre)
            init {
                itemView.setOnClickListener{v:View->
                    val position: Int=adapterPosition
                    Toast.makeText(itemView.context, "you click on item # ${position+ 1}", Toast.LENGTH_SHORT).show()

                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.card_categoria_sonido,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemNombre.text=sonidos[position]
    }

    override fun getItemCount(): Int {
        return sonidos.size
    }

}
