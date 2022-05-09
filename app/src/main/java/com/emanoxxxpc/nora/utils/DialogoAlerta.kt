package com.emanoxxxpc.nora.utils

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.emanoxxxpc.nora.R

class DialogoAlerta {


    companion object {
        fun nuevaAlertaSimple(context: Context, titulo: String, mensaje: String): AlertDialog {
            var alertaSimple =  AlertDialog.Builder(context).apply {
                setPositiveButton(
                    R.string.accept,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
                setNegativeButton(
                    R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }.create()
            alertaSimple.setCancelable(true)
            alertaSimple.setCanceledOnTouchOutside(true)
            alertaSimple.setTitle(titulo)
            alertaSimple.setMessage(mensaje)
            alertaSimple.show()
            return alertaSimple
        }

        fun nuevaAlertaConVista(context: Context, titulo: String, mensaje: String, view: View): AlertDialog {
            var alertaSimple =  AlertDialog.Builder(context).apply {
                setView(view)
                setPositiveButton(
                    R.string.accept,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
                setNegativeButton(
                    R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }.create()
            alertaSimple.setCancelable(true)
            alertaSimple.setCanceledOnTouchOutside(true)
            alertaSimple.setTitle(titulo)
            alertaSimple.setMessage(mensaje)
            alertaSimple.show()
            return alertaSimple
        }
    }
}