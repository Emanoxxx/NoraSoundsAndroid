package com.emanoxxxpc.nora

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.getIntent
import android.content.Intent.makeRestartActivityTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.emanoxxxpc.nora.models.CategoriaDeSonido
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.internal.ContextUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RV_archivos_Adapter(
    var categoria: CategoriaDeSonido,
    var host: String,
    var token: String,
    var activity: Activity
):

    RecyclerView.Adapter<RV_archivos_Adapter.ViewHolder>(){
    var noraApi = NoraApiService.getApiSession(host)
    inner class ViewHolder(itemView: View ): RecyclerView.ViewHolder(itemView){
        val itemNombre:TextView=itemView.findViewById(R.id.cv_nombre)
        val playbutton:FloatingActionButton=itemView.findViewById(R.id.RV_archivo_reproducir);
        val deleteButton:FloatingActionButton=itemView.findViewById(R.id.RV_archivo_borrar_button);
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
            var aceptarDialog: AlertDialog =AlertDialog.Builder(activity).apply {
                setPositiveButton(R.string.accept,
                DialogInterface.OnClickListener { dialog, id ->
                    deleteArchivo(categoria.archivos!![position])
                })
                setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }.create()
            aceptarDialog.setCancelable(true)
            aceptarDialog.setTitle("Borrar Archivo ")
            aceptarDialog.setMessage("¿Seguro que desea borrar ${categoria.archivos!![position]}?")
            aceptarDialog.show()

        }
    }

    override fun getItemCount(): Int {
        return categoria.archivos!!.size
    }
    fun deleteArchivo(archivo: String){
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta = noraApi.deleteArchivo("$token",categoria.id!!,archivo)
            if (!respuesta.isSuccessful) {
                val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                activity.runOnUiThread {
                    Toast.makeText(
                        activity,
                        responseError.error,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                return@launch
            }
            val categoriaDeSonido:CategoriaDeSonido = respuesta.body()!!

            activity.runOnUiThread {
                val intent = Intent(activity, CategoriaSonido::class.java).apply {
                    putExtra("IDCategoria", categoria.id)
                }
                startActivity(activity, intent, Bundle.EMPTY)
                activity.finish()
            }


        }
    }


}
