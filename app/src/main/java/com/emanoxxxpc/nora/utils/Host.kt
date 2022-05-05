package com.emanoxxxpc.nora.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.emanoxxxpc.nora.SetHost

class Host {
    companion object {
        fun verifyHost(hostPreferences:SharedPreferences,context:Context):String?{
            val host = hostPreferences.getString("Host", null)
            if (host == null) {
                val intent = Intent(context, SetHost::class.java)
                startActivity(context,intent,null)
                return null
            }
            return host
        }
        fun changeHost(context: Context){
            val intent = Intent(context, SetHost::class.java)
            startActivity(context,intent,null)
        }

    }
}