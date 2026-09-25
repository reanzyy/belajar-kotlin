package com.latihan.belajarstorage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnMenu1 = findViewById<Button>(R.id.btnMenu1)
        val btnMenu2 = findViewById<Button>(R.id.btnMenu2)
        val btnMenu3 = findViewById<Button>(R.id.btnMenu3)
        val btnMenu4 = findViewById<Button>(R.id.btnMenu4)

        // Navigasi ke Halaman Simpan 1 Data (MainActivity)
        btnMenu1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Navigasi ke Halaman Simpan Banyak Data (MultiDataActivity)
        btnMenu2.setOnClickListener {
            val intent = Intent(this, MultiDataActivity::class.java)
            startActivity(intent)
        }

        // Navigasi ke Halaman Data List View
        btnMenu3.setOnClickListener {
            val intent = Intent(this, DataListViewActivity::class.java)
            startActivity(intent)
        }

        // Navigasi ke Halaman Splash Screen (untuk testing)
        btnMenu4.setOnClickListener {
            val intent = Intent(this, SplashScreenActivity::class.java)
            startActivity(intent)
        }
    }
}