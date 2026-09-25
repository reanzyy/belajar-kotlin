package com.latihan.belajarstorage

import android.app.Activity // Mengimpor class Activity
import android.os.Bundle // Mengimpor Bundle
import android.widget.ArrayAdapter // Mengimpor adapter bawaan
import android.widget.ListView // Mengimpor ListView
import android.widget.Toast // Mengimpor Toast

class DataListViewActivity : Activity() { // Membuat Activity utama

    override fun onCreate(savedInstanceState: Bundle?) { // Method saat Activity dibuat
        super.onCreate(savedInstanceState) // Menjalankan proses bawaan Activity

        setContentView(R.layout.activity_main) // Memasang layout utama

        val list = findViewById<ListView>(R.id.lvDasar) // Mengambil ListView dari XML

        val data = arrayOf( // Membuat array data nama mahasiswa
            "Andi", // Data mahasiswa pertama
            "Budi", // Data mahasiswa kedua
            "Citra", // Data mahasiswa ketiga
            "Dedi", // Data mahasiswa keempat
            "Eka" // Data mahasiswa kelima
        ) // Menutup array data

        val adapter = ArrayAdapter( // Membuat adapter untuk ListView
            this, // Context Activity saat ini
            android.R.layout.simple_list_item_1, // Layout teks bawaan Android
            data // Data yang akan ditampilkan
        ) // Menutup pembuatan adapter

        list.adapter = adapter // Menghubungkan adapter ke ListView

        list.setOnItemClickListener { _, _, position, _ -> // Menangani klik item
            val nama = data[position] // Mengambil nama berdasarkan posisi item
            Toast.makeText(this, nama, Toast.LENGTH_SHORT).show() // Menampilkan pesan nama
        } // Menutup listener klik

    } // Menutup method onCreate

} // Menutup class MainActivity