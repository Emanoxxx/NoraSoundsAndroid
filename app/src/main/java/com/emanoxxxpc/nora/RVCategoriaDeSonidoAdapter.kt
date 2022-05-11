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
import androidx.recyclerview.widget.RecyclerView
import com.emanoxxxpc.nora.models.CategoriaDeSonido
import com.emanoxxxpc.nora.utils.Player
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RVCategoriaDeSonidoAdapter(
    private var sonidos: List<CategoriaDeSonido>,
    private var activity: Activity,
    var host: String
) :
    RecyclerView.Adapter<RVCategoriaDeSonidoAdapter.ViewHolder>() {
    @Suppress("DEPRECATION")
    inner class ViewHolder(itemView: View, activity: Activity) : RecyclerView.ViewHolder(itemView) {
        val itemNombre: TextView = itemView.findViewById(R.id.cv_nombre)
        var itemCategoria: CategoriaDeSonido? = null
        val playbutton: FloatingActionButton = itemView.findViewById(R.id.reproducir)

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                val intent = Intent(activity, CategoriaDeSonidoActivity::class.java).apply {
                    putExtra("IDCategoria", sonidos[position].id)
                }
                startActivity(activity, intent, Bundle.EMPTY)
            }


            playbutton.setOnClickListener {
                if (itemCategoria!!.archivos != null) {
                    if (!Player.playSound(
                            itemCategoria!!.archivos!!,
                            itemCategoria!!.id!!,
                            host
                        )
                    ) {
                        Toast.makeText(
                            activity,
                            "No encontre archivo a reproducir",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        return@setOnClickListener
                    }
                } else {
                    Toast.makeText(activity, "No encontre archivo a reproducir", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_categoria_sonido, parent, false)
        return ViewHolder(v, activity)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemNombre.text = sonidos[position].nombre
        holder.itemCategoria = sonidos[position]
        if (sonidos[position].archivos != null) {
            holder.playbutton.isEnabled = sonidos[position].archivos!!.isNotEmpty()
        } else {
            holder.playbutton.isEnabled = false
        }

    }

    override fun getItemCount(): Int {
        return sonidos.size
    }

}
