package com.emanoxxxpc.nora

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.emanoxxxpc.nora.models.CategoriaDeSonido
import com.emanoxxxpc.nora.models.Comando
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray


class comandofragment(categoria:CategoriaDeSonido, var host:String, var token:String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var categoria:CategoriaDeSonido=categoria
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_comandofragment, container, false)
        recyclerView = rootView.findViewById(R.id.rv_comandos) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RV_Comandos_Adapter(categoria,activity,host,token);

        return rootView
    }

    override fun onStart() {
        super.onStart()
        val inflater = requireActivity().layoutInflater;
        val alerta:View=inflater.inflate(R.layout.dialog_add, null)
        alerta.findViewById<EditText>(R.id.et_comando).setHint(R.string.add_Comando)
        addButton=requireActivity().findViewById(R.id.fab_add_Comando);
        addButton.setOnClickListener {v:View->
            var aceptarDialog: AlertDialog = AlertDialog.Builder(requireActivity()).apply {
                setView(alerta)

                setPositiveButton(R.string.add,
                    DialogInterface.OnClickListener { dialog, id ->
                    addComando(alerta.findViewById<EditText>(R.id.et_comando).text.toString())
                    })
                setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }.create()
            aceptarDialog.setCancelable(true)
            aceptarDialog.setTitle("Agregar Comando")
            aceptarDialog.show()

        }
    }

    private fun addComando(comando:String) {
        if(comando==""){
            Toast.makeText(activity, "No valido, campo vacio.", Toast.LENGTH_SHORT).show()
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta = NoraApiService.getApiSession(host).addComando(token,categoria.id!!,
                Comando(comando)
            )
            if (!respuesta.isSuccessful) {
                val responseError = ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        activity,
                        responseError.error,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                return@launch
            }

            requireActivity().runOnUiThread {
                val intent = Intent(activity, CategoriaSonido::class.java).apply {
                    putExtra("IDCategoria", categoria.id)
                }
                ContextCompat.startActivity(requireActivity(), intent, Bundle.EMPTY)
                requireActivity().finish()
            }


        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}