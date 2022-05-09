package com.emanoxxxpc.nora

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.emanoxxxpc.nora.models.CategoriaDeSonido
import org.json.JSONArray

class ViewPagerAdapter(
    fragment: FragmentActivity,
    categoria: CategoriaDeSonido,
    var host: String,
    var token: String
):FragmentStateAdapter(fragment) {
    companion object{
        private const val ARG_OBJECT="object"
    }
    private var categoria:CategoriaDeSonido=categoria
    override fun getItemCount(): Int =2
    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> {
                val fragment=comandofragment(categoria, host ,token)
                return fragment
            }
            1 -> {
                val fragment=archivofragment(categoria, host ,token)
                return fragment
            }
            else ->{
                return Fragment()
            }
        }


    }
}