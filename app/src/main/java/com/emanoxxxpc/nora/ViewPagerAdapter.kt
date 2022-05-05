package com.emanoxxxpc.nora

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: FragmentActivity):FragmentStateAdapter(fragment) {
    companion object{
        private const val ARG_OBJECT="object"
    }
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                val fragment=comandofragment()
                return fragment
            }
            1 -> {
                val fragment=csfragment()
                fragment.arguments= Bundle().apply{
                    putInt(ARG_OBJECT,position+1)
                }
                return fragment
            }
            else->{
                val fragment=csfragment()
                fragment.arguments= Bundle().apply{
                    putInt(ARG_OBJECT,position+1)
                }
                return fragment
            }
        }


    }

}