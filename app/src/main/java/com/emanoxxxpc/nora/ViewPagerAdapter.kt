package com.emanoxxxpc.nora

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.emanoxxxpc.nora.models.CategoriaDeSonido

class ViewPagerAdapter(
    fragment: FragmentActivity,
    private var categoria: CategoriaDeSonido,
    var host: String,
    var token: String
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> {
                val fragment = ComandoFragment(categoria, host, token)
                fragment
            }
            1 -> {
                val fragment = ArchivoFragment(categoria, host, token)
                fragment
            }
            else -> {
                Fragment()
            }
        }


    }
}