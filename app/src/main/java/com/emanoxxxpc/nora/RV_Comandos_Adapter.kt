package com.emanoxxxpc.nora

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.emanoxxxpc.nora.models.CategoriaDeSonido
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class RV_Comandos_Adapter(var categoria: CategoriaDeSonido, var activity: FragmentActivity?, var host: String,
                          var token: String):
    RecyclerView.Adapter<RV_Comandos_Adapter.ViewHolder>(){

    inner class ViewHolder(itemView: View ): RecyclerView.ViewHolder(itemView){
        val itemNombre:TextView=itemView.findViewById(R.id.cv_nombre)
        val deleteButton: FloatingActionButton =itemView.findViewById(R.id.borrar_button2);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ViewHolder {
        val v=LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_comando_item,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemNombre.text=categoria.comandos!![position]
        holder.deleteButton.setOnClickListener{v:View->
            var aceptarDialog: AlertDialog = AlertDialog.Builder(activity!!).apply {
                setPositiveButton(R.string.accept,
                    DialogInterface.OnClickListener { dialog, id ->
                        deleteComando(categoria.comandos!![position])
                    })
                setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }.create()
            aceptarDialog.setCancelable(true)
            aceptarDialog.setTitle("Borrar Archivo ")
            aceptarDialog.setMessage("¿Seguro que desea borrar ${categoria.comandos!![position]}?")
            aceptarDialog.show()

        }
    }

    override fun getItemCount(): Int {
        return categoria.comandos!!.size
    }
    fun deleteComando(comando: String){
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta = NoraApiService.getApiSession(host).deleteComando("$token",categoria.id!!,comando)
            if (!respuesta.isSuccessful) {
                val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                activity!!.runOnUiThread {
                    Toast.makeText(
                        activity,
                        responseError.error,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                return@launch
            }

            activity!!.runOnUiThread {
                val intent = Intent(activity, CategoriaSonido::class.java).apply {
                    putExtra("IDCategoria", categoria.id)
                }
                ContextCompat.startActivity(activity!!, intent, Bundle.EMPTY)
                activity!!.finish()
            }


        }
    }
}