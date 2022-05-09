package com.emanoxxxpc.nora

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emanoxxxpc.nora.models.CategoriaDeSonido


class archivofragment(
    categoriaDeSonido: CategoriaDeSonido,
    var host: String,
    var token: String,
) : Fragment() {
    // TODO: Rename and change types of parameters
    private var categoria: CategoriaDeSonido = categoriaDeSonido

    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_archivofragment, container, false)
        recyclerView = rootView.findViewById(R.id.rv_archivos) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RV_archivos_Adapter(categoria,host,token,activity!!);
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}