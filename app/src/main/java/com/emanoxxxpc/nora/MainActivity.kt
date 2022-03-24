package com.emanoxxxpc.nora

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        var button:Button=findViewById(R.id.button);
        button.setOnClickListener{
            val intent = Intent(this, CategoriaSonido::class.java)
            startActivity(intent);
        }
        supportActionBar!!.setDisplayShowTitleEnabled(false)

    }
}