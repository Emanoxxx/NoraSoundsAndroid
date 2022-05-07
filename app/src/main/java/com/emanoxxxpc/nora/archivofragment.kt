package com.emanoxxxpc.nora

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emanoxxxpc.nora.models.CategoriaDeSonido
import org.json.JSONArray


class archivofragment(categoriaDeSonido: CategoriaDeSonido, var host: String, var token: String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var categoria: CategoriaDeSonido = categoriaDeSonido

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_archivofragment, container, false)
        recyclerView = rootView.findViewById(R.id.rv_archivos) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RV_archivos_Adapter(categoria,host,token);
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}