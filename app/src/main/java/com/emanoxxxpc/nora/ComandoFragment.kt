package com.emanoxxxpc.nora

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.emanoxxxpc.nora.utils.DialogoAlerta
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ComandoFragment(
    private var categoria: CategoriaDeSonido,
    var host: String,
    var token: String
) :
    Fragment() {
    // TODO: Rename and change types of parameters
    //private var categoria:CategoriaDeSonido=categoria
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_comandofragment, container, false)
        recyclerView = rootView.findViewById(R.id.rv_comandos) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RVComandosAdapter(categoria, activity, host, token)

        return rootView
    }

    @SuppressLint("InflateParams")
    override fun onStart() {
        super.onStart()
        addButton = requireActivity().findViewById(R.id.fab_add_Comando)
        addButton.setOnClickListener {
            val alerta: View = requireActivity().layoutInflater.inflate(R.layout.dialog_add, null)
            alerta.findViewById<EditText>(R.id.et_comando).setHint(R.string.add_Comando)
            val agregarComandoDialog = DialogoAlerta.nuevaAlertaConVista(
                requireActivity(), "Agregar comando", "Ingrese el nuevo comando", alerta
            )
            val btnAceptar = agregarComandoDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val etComando = agregarComandoDialog.findViewById<EditText>(R.id.et_comando)!!

            btnAceptar.setOnClickListener {
                etComando.error = null
                val comando = etComando.text.toString()

                if (comando == "") {
                    etComando.error = "Ingrese el comando"
                    return@setOnClickListener
                }
                CoroutineScope(Dispatchers.IO).launch {
                    val respuesta = NoraApiService.getApiSession(host).addComando(
                        token, categoria.id!!,
                        Comando(comando)
                    )
                    if (!respuesta.isSuccessful) {
                        val responseError =
                            ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                        if (responseError.code == "P2002") {
                            requireActivity().runOnUiThread {
                                Toast.makeText(
                                    activity,
                                    responseError.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                                etComando.error = "Comando ya registrado"
                            }
                            return@launch
                        }

                        requireActivity().runOnUiThread {
                            Toast.makeText(
                                activity,
                                "Algo salió mal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@launch
                    }
                    val categoriaDeSonido = respuesta.body()!!
                    requireActivity().runOnUiThread {

                        recyclerView.adapter = RVComandosAdapter(categoriaDeSonido, activity, host, token)
                        agregarComandoDialog.dismiss()
                        agregarComandoDialog.hide()
                    }
                }

            }
        }
    }

}