package com.emanoxxxpc.nora

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.emanoxxxpc.nora.models.CategoriaDeSonido
import org.json.JSONArray
import org.json.JSONObject

class ViewPagerAdapter(fragment: FragmentActivity,categoria:JSONObject, var host: String, var token: String):FragmentStateAdapter(fragment) {
    companion object{
        private const val ARG_OBJECT="object"
    }
    private var categoria:JSONObject=categoria
    override fun getItemCount(): Int =2
    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> {
                val fragment=comandofragment(JSONArray( categoria.get("comandos").toString()))
                return fragment
            }
            1 -> {
                val fragment=archivofragment(CategoriaDeSonido.fromJSON(categoria.toString()), host ,token)
                return fragment
            }
            else ->{
                return Fragment()
            }
        }


    }
}