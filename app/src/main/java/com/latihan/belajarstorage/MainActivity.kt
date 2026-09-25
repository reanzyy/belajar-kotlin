package com.latihan.belajarstorage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    // 1. Deklarasi variabel SharedPreferences
    private val PREF_NAME = "MyUserPrefs"
    private val KEY_NAMA = "KEY_NAMA_USER"
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. inisialisasi SharedPreferences dengan mode private
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // Hubungkan widget antarmuka
        val etNama = findViewById<EditText>(R.id.etNama)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnMuat = findViewById<Button>(R.id.btnMuat)
        val btnHapus = findViewById<Button>(R.id.btnHapus)
        val tvHasil = findViewById<TextView>(R.id.tvHasil)

        // 3. Operasi Menulis Data (Write) saat tombol Simpan di tekan
        btnSimpan.setOnClickListener {
            val inputNama = etNama.text.toString()
            if(inputNama.isNotEmpty()) {
                val editor = sharedPreferences.edit()
                editor.putString(KEY_NAMA, inputNama)
                editor.apply()  // Menyimpan secara asynchronous
                Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                etNama.text.clear()
            } else {
                Toast.makeText(this, "Silahkan isi nama terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
        btnMuat.setOnClickListener {
            val dataTersimpan = sharedPreferences.getString(KEY_NAMA, null)
            if (!dataTersimpan.isNullOrEmpty()) {
                tvHasil.text = "Selamat Datang Kembali: $dataTersimpan"
                Toast.makeText(this, "Data berhasil dimuat!", Toast.LENGTH_SHORT).show()
            } else {
                tvHasil.text = "[Data tidak ditemukan]"
                Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
        btnHapus.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.remove(KEY_NAMA)  // Menghapus key Spesifik
            editor.apply()

            tvHasil.text = "[Data telah dihapus]"
            Toast.makeText(this,
                "Data berhasil dihapus",
                Toast.LENGTH_SHORT).show()
        }
        // 6. Memuat data otomatis saat pertama kali aplikasi dijalankan
        // Jika file MyUserPrefs.xml belum pernah dibuat, sistem akan otomatis menginisialisasi nilai default
        if (!sharedPreferences.contains(KEY_NAMA)) {
            sharedPreferences.edit()
                .putString(KEY_NAMA, "Ahmad Fauzi (Data Default)")
                .apply()
        }

        val namaAwal = sharedPreferences.getString(KEY_NAMA, null)
        if (namaAwal != null) {
            tvHasil.text = "Selamat Datang Kembali: $namaAwal"
        }

    }
}