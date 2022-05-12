package com.emanoxxxpc.nora

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.emanoxxxpc.nora.models.CategoriaDeSonido
import com.emanoxxxpc.nora.models.Usuario
import com.emanoxxxpc.nora.utils.Player
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response


class RVSolicitudesAdapter(
    var host: String,
    var token: String,
    var usuarios:MutableList<Usuario>,
    private var activity: Activity
) :

    RecyclerView.Adapter<RVSolicitudesAdapter.ViewHolder>() {
    private var noraApi = NoraApiService.getApiSession(host)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tv_nombre)
        val tvEmail: TextView = itemView.findViewById(R.id.tv_email)
        val switchActivo: Switch =itemView.findViewById(R.id.isActive_Switch)
        val switchAdmin: Switch =itemView.findViewById(R.id.isAdmin_Switch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_solicitud_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvNombre.text=usuarios[position].username
        holder.tvEmail.text=usuarios[position].email
        holder.switchActivo.isChecked=usuarios[position].isActive!!
        holder.switchAdmin.isChecked=usuarios[position].isAdmin!!
        holder.switchActivo.setOnClickListener(){
            activar(!holder.switchActivo.isChecked,usuarios[position].username,holder.switchActivo)
        }
        holder.switchAdmin.setOnClickListener(){
            toAdmin(!holder.switchAdmin.isChecked,usuarios[position].username,holder.switchAdmin)
        }
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }
    fun activar(activar:Boolean,nombre:String,switch: Switch){
        switch.isEnabled=false
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta: Response<Usuario>
            if(!activar){
                respuesta = NoraApiService.getApiSession(host).activarUsuario(token,nombre)
            }else{
                respuesta = NoraApiService.getApiSession(host).desactivarUsuario(token,nombre)
            }
            if (!respuesta.isSuccessful) {
                val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                activity.runOnUiThread {
                    Toast.makeText(
                        activity,
                        responseError.error,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    switch.isChecked=activar
                    switch.isEnabled=true
                }

                return@launch
            }
            activity.runOnUiThread {
                Toast.makeText(activity, "Usuario " + nombre + " actualizado", Toast.LENGTH_SHORT)
                    .show()
                switch.isEnabled=true
            }

        }
    }
    fun toAdmin(activar:Boolean,nombre:String,switch: Switch){
        switch.isEnabled=false
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta: Response<Usuario>
            if(!activar){
                respuesta = NoraApiService.getApiSession(host).toadmin(token,nombre)
            }else{
                respuesta = NoraApiService.getApiSession(host).toUser(token,nombre)
            }
            if (!respuesta.isSuccessful) {
                val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                activity.runOnUiThread {
                    Toast.makeText(
                        activity,
                        responseError.error,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    switch.isChecked=activar
                    switch.isEnabled=true
                }

                return@launch
            }
            activity.runOnUiThread {
                Toast.makeText(activity, "Usuario " + nombre + " actualizado", Toast.LENGTH_SHORT)
                    .show()
                switch.isEnabled=true
            }

        }
    }

}
