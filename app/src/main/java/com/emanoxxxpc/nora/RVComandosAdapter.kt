package com.emanoxxxpc.nora

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.emanoxxxpc.nora.models.CategoriaDeSonido
import com.emanoxxxpc.nora.models.Comando
import com.emanoxxxpc.nora.utils.DialogoAlerta
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RVComandosAdapter(
    private var categoria: CategoriaDeSonido,
    private var activity: FragmentActivity?,
    var host: String,
    var token: String
) :
    RecyclerView.Adapter<RVComandosAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNombre: TextView = itemView.findViewById(R.id.cv_nombre)
        val deleteButton: FloatingActionButton = itemView.findViewById(R.id.borrar_button2)
        val btnEditarComando: FloatingActionButton = itemView.findViewById(R.id.btn_editar_comando)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_comando_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemNombre.text = categoria.comandos!![position]
        val comando = categoria.comandos!![position]
        holder.deleteButton.setOnClickListener {
            val eliminarDialog = DialogoAlerta.nuevaAlertaSimple(
                activity!!,
                "Eliminar comando",
                "¿Seguro que desea elimiar el comando $comando?"
            )
            eliminarDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val respuesta = NoraApiService.getApiSession(host).deleteComando(token, categoria.id!!, comando)
                    if (!respuesta.isSuccessful) {
                        val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                        activity!!.runOnUiThread {
                            Toast.makeText(activity!!, "Algo salió mal", Toast.LENGTH_SHORT).show()
                        }
                        return@launch
                    }
                    val categoriaDeSonido = respuesta.body()!!
                    activity!!.runOnUiThread {
                        Toast.makeText(activity!!, "${comando} eliminado.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, CategoriaDeSonidoActivity::class.java).apply {
                            putExtra("IDCategoria", categoriaDeSonido.id)
                        }
                        ContextCompat.startActivity(activity!!, intent, Bundle.EMPTY)
                        activity!!.finish()
                        eliminarDialog.dismiss()
                    }
                }
            }

        }

        holder.btnEditarComando.setOnClickListener {
            val activityEditar =
                LayoutInflater.from(activity!!).inflate(R.layout.fragment_editar_comando, null)
            val editarDialog = DialogoAlerta.nuevaAlertaConVista(
                activity!!,
                "Editar comando",
                "Ingrese el nuevo comando.",
                activityEditar
            )
            val etNuevoComando: EditText = editarDialog.findViewById(R.id.et_nuevo_comando)!!
            etNuevoComando.setText(comando)
            editarDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val actividad: Activity = activity!!
                etNuevoComando.error = null
                val nuevoComando = etNuevoComando.text.toString()
                if (nuevoComando == "") {
                    etNuevoComando.error = "Ingrese el comando."
                    return@setOnClickListener
                }

                if (nuevoComando == comando) {
                    etNuevoComando.error = "No ha cambiado el comando."
                    return@setOnClickListener
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val respuesta = NoraApiService.getApiSession(host).editarComando(
                        token,
                        categoria.id!!,
                        categoria.comandos!![position],
                        Comando(nuevoComando)
                    )
                    if (!respuesta.isSuccessful) {
                        val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                        if(responseError.code == "P2002") {
                            actividad.runOnUiThread {
                                Toast.makeText(actividad, responseError.error, Toast.LENGTH_SHORT).show()
                                etNuevoComando.error = "Comando ya registrado"
                            }
                            return@launch
                        }
                        actividad.runOnUiThread {
                            Toast.makeText(actividad, "Algo salió mal", Toast.LENGTH_SHORT).show()
                        }
                        return@launch
                    }

                    actividad.runOnUiThread {
                        holder.itemNombre.text = nuevoComando
                        Toast.makeText(actividad, "$nuevoComando actualizado.", Toast.LENGTH_SHORT).show()
                    }
                    editarDialog.dismiss()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return categoria.comandos!!.size
    }

    private fun deleteComando(comando: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta =
                NoraApiService.getApiSession(host).deleteComando(token, categoria.id!!, comando)
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
                val intent = Intent(activity, CategoriaDeSonidoActivity::class.java).apply {
                    putExtra("IDCategoria", categoria.id)
                }
                ContextCompat.startActivity(activity!!, intent, Bundle.EMPTY)
                activity!!.finish()
            }


        }
    }
}