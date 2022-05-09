package com.emanoxxxpc.nora

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emanoxxxpc.nora.models.CategoriaDeSonido


class ArchivoFragment(
    var categoriaDeSonido: CategoriaDeSonido,
    var host: String,
    var token: String,
) : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var recyclerView: RecyclerView
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

}