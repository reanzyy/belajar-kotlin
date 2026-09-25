package com.latihan.belajarstorage

/**
 * Model data untuk merepresentasikan entitas data yang disimpan pada SharedPreferences
 * @property id identifier unik berbasis timestamp milidetik
 * @property nama nama data yang dimasukkan oleh pengguna
 */
data class DataItem(
    val id: String = System.currentTimeMillis().toString(),
    var nama: String
)
