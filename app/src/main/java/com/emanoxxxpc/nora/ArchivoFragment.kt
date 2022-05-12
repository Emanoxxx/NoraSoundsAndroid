package com.emanoxxxpc.nora

import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream


class ArchivoFragment(
    var categoriaDeSonido: CategoriaDeSonido,
    var host: String,
    var token: String,
) : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var body: MultipartBody.Part
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton : FloatingActionButton
    private lateinit var alerta: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_archivofragment, container, false)
        recyclerView = rootView.findViewById(R.id.rv_archivos) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RVArchivosAdapter(categoriaDeSonido, host, token, requireActivity())
        return rootView
    }

    override fun onStart() {
        super.onStart()
        addButton = requireActivity().findViewById(R.id.fab_add_Archivos)
        addButton.setOnClickListener {
            alerta= requireActivity().layoutInflater.inflate(R.layout.dialog_add_archivo, null)
            val agregarArchivoDialog = DialogoAlerta.nuevaAlertaConVista(
                requireActivity(), "Agregar Archivo", "Seleccione Archivo a agregar", alerta
            )
            val btnAceptar = agregarArchivoDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val btnCancelar = agregarArchivoDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            val btnAgregar=alerta.findViewById<Button>(R.id.select_Archivo)
            btnAgregar.setOnClickListener {
                    val intent_upload = Intent()
                    intent_upload.type = "audio/*"
                    intent_upload.action = Intent.ACTION_GET_CONTENT
                    this@ArchivoFragment.startActivityForResult(Intent.createChooser(intent_upload,"Elige archivo de audio"),1)


            }
            btnAceptar.setOnClickListener {
                btnAceptar.isEnabled=false
                btnAgregar.isEnabled=false
                btnCancelar.isEnabled=false
                alerta.isEnabled=false
                var archivoText: TextView =alerta.findViewById(R.id.t_archivo)
                CoroutineScope(Dispatchers.IO).launch {
                    var noraApi = NoraApiService.getApiSession(host)
                    val respuesta = noraApi.addArchivo(token, categoriaDeSonido.id!!, body)
                    if (!respuesta.isSuccessful) {
                        requireActivity().runOnUiThread {
                            btnAceptar.isEnabled=true
                            btnAgregar.isEnabled=true
                            btnCancelar.isEnabled=true
                        }

                        val responseError =
                            ResponseError.parseResponseErrorBody(respuesta.errorBody()!!)
                        if (responseError.code == "P2002") {
                            requireActivity().runOnUiThread {
                                Toast.makeText(
                                    activity,
                                    responseError.error,
                                    Toast.LENGTH_SHORT
                                ).show()

                                archivoText.error = "Archivo ya registrado"
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

                        startActivity(Intent(this@ArchivoFragment.context,CategoriaDeSonidoActivity::class.java).apply { putExtra("IDCategoria", categoriaDeSonido.id) })
                        Toast.makeText(requireActivity(), "${archivoText.text.toString()} Agregado con exito", Toast.LENGTH_SHORT).show()
                        requireActivity().finish()
                    }
                }


            }
        }
    }

    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === RESULT_CANCELED) {
            return
        }
        if (resultCode === RESULT_OK && requestCode === 1) {


            var uri:Uri = data!!.data!!;

            var nombreFile:String=dumpAudioMetaData(uri)!!
            var archivoText: TextView =alerta.findViewById(R.id.t_archivo)
            archivoText.text=nombreFile
                //Procesar el resultado

                var  reqBody: RequestBody = RequestBody.create(MediaType.parse("audio/*"),getBitmapFromUri(uri))
                body=MultipartBody.Part.createFormData("audio",nombreFile,reqBody)

        }

    }


    @SuppressLint("Range")
    fun dumpAudioMetaData(uri: Uri):String? {
        val contentResolver = requireActivity().contentResolver
        val cursor: Cursor? = contentResolver.query(
            uri, null, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val displayName: String =
                    it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                return displayName


            }
        }
        return null
    }
    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri): ByteArray? {
        val contentResolver = requireActivity().contentResolver
        var iStream:InputStream=contentResolver.openInputStream(uri)!!
        var inputData:ByteArray
        inputData=getBytes(iStream)!!
        return inputData
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }


}