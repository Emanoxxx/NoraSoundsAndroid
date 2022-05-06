package com.emanoxxxpc.nora

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray


class comandofragment(comandos:JSONArray) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: JSONArray = comandos

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_comandofragment, container, false)
        recyclerView = rootView.findViewById(R.id.rv_comandos) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RV_Comandos_Adapter(param1);
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set a LinearLayoutManager to handle Android
        // RecyclerView behavior
        println(param1)

    }


}