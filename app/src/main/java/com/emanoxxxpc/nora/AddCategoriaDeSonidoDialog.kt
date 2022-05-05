package com.emanoxxxpc.nora

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.emanoxxxpc.nora.api.NoraApiService
import com.emanoxxxpc.nora.api.ResponseError
import com.emanoxxxpc.nora.models.CategoriaDeSonido
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [AddCategoriaDeSonidoDialog.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddCategoriaDeSonidoDialog : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var token: String? = null
    private var host: String? = null
    private var etNombre: EditText? = null
    private var btnSubmit: Button? = null
    private var btnCancel: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            host = requireArguments().getString(ARG_PARAM1)
            token = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val noraApi = NoraApiService.getApiSession(host!!)
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_add_categoria_de_sonido, container, false)
        etNombre = v.findViewById(R.id.nombre_et)
        btnSubmit = v.findViewById(R.id.add_btn)
        btnCancel = v.findViewById(R.id.cancel_btn)
        btnSubmit!!.setOnClickListener(View.OnClickListener { view: View? ->
            etNombre!!.error = null
            val nombre = etNombre!!.getText().toString()
            if (nombre == "") {
                Toast.makeText(v.context, "Por favor ingrese el nombre", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val categoriaDeSonido = CategoriaDeSonido(nombre)
            CoroutineScope(Dispatchers.IO).launch {
//                Looper.prepare()
                val respuesta =
                    noraApi.createCategoriaDeSonido(token!! as String, categoriaDeSonido)
                if (!respuesta.isSuccessful) {
                    val responseError =
                        ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                    activity?.runOnUiThread {
                        if (responseError.code != null) {
                            when (responseError.code) {
                                "P2002" -> {
                                    Toast.makeText(
                                        this@AddCategoriaDeSonidoDialog.context,
                                        "Ya existe una categoría con el nombre $nombre",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                    etNombre!!.error = "Nombre ya registrado."
                                }
                            }
                        }
                    }
                    return@launch
                }
                activity?.runOnUiThread {
                    Toast.makeText(this@AddCategoriaDeSonidoDialog.context, "$nombre agregada.", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this@AddCategoriaDeSonidoDialog.context, Catalogo_Categorias::class.java))
                }
                dismiss()
            }

        })
        btnCancel!!.setOnClickListener(View.OnClickListener { view: View? -> dismiss() })
        return v
    }

    companion object {
        private const val ARG_PARAM1 = "host"
        private const val ARG_PARAM2 = "token"
        const val TAG = "ADD_CATEGORIA_DE_SONIDO_DIALOG"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param host Host del servicio api.
         * @param token Access token del servicio api.
         * @return A new instance of fragment EditReview.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(host: String?, token: String?): AddCategoriaDeSonidoDialog {
            val fragment = AddCategoriaDeSonidoDialog()
            val args = Bundle()
            args.putString(ARG_PARAM1, host)
            args.putString(ARG_PARAM2, token)
            fragment.arguments = args
            return fragment
        }
    }
}