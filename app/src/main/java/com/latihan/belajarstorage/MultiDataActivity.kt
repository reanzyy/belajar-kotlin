package com.latihan.belajarstorage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

/**
 * MultiDataActivity: Mengelola banyak data pada SharedPreferences (File XML)
 * Dilengkapi RecyclerView interaktif dengan operasi CRUD (Tambah, Tampil, Ubah, Hapus)
 * yang selalu tersinkronisasi secara real-time.
 */
class MultiDataActivity : AppCompatActivity() {

    // Konfigurasi SharedPreferences
    private val PREF_NAME = "MyMultiUserPrefs"
    private val KEY_LIST_DATA = "KEY_LIST_DATA"
    private lateinit var sharedPreferences: SharedPreferences

    // Komponen Tampilan
    private lateinit var etInputData: EditText
    private lateinit var btnSimpanData: Button
    private lateinit var btnBatalEdit: Button
    private lateinit var tvFormTitle: TextView
    private lateinit var tvTotalData: TextView
    private lateinit var tvKosong: TextView
    private lateinit var rvData: RecyclerView

    // Adapter dan Sumber Data In-Memory
    private val listData = ArrayList<DataItem>()
    private lateinit var adapter: DataAdapter

    // State untuk Operasi Ubah / Edit
    private var editingPosition: Int = -1
    private var editingItem: DataItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_multi_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // 2. Hubungkan Widget UI
        initViews()

        // 3. Setup RecyclerView dan Adapter
        setupRecyclerView()

        // 4. Muat Data Tersimpan dari XML SharedPreferences
        loadDataFromPreferences()

        // 5. Setup Event Listener untuk Tombol-Tombol
        setupListeners()
    }

    private fun initViews() {
        etInputData = findViewById(R.id.etInputData)
        btnSimpanData = findViewById(R.id.btnSimpanData)
        btnBatalEdit = findViewById(R.id.btnBatalEdit)
        tvFormTitle = findViewById(R.id.tvFormTitle)
        tvTotalData = findViewById(R.id.tvTotalData)
        tvKosong = findViewById(R.id.tvKosong)
        rvData = findViewById(R.id.rvData)
    }

    private fun setupRecyclerView() {
        adapter = DataAdapter(
            listData = listData,
            onEditClick = { item, position ->
                mulaiEditData(item, position)
            },
            onDeleteClick = { item, position ->
                konfirmasiHapusData(item, position)
            }
        )

        rvData.layoutManager = LinearLayoutManager(this)
        rvData.adapter = adapter
    }

    private fun setupListeners() {
        // Tombol Kembali ke Halaman Menu
        findViewById<Button>(R.id.btnKembali).setOnClickListener {
            finish()
        }

        // Tombol Tambah / Update Data
        btnSimpanData.setOnClickListener {
            simpanAtauUpdateData()
        }

        // Tombol Batal Edit
        btnBatalEdit.setOnClickListener {
            resetFormState()
        }
    }

    /**
     * Memuat daftar data dari file XML SharedPreferences (format JSON Array)
     */
    private fun loadDataFromPreferences() {
        listData.clear()
        val jsonString = sharedPreferences.getString(KEY_LIST_DATA, null)

        if (!jsonString.isNullOrEmpty()) {
            try {
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val id = obj.optString("id", System.currentTimeMillis().toString())
                    val nama = obj.optString("nama", "")
                    listData.add(DataItem(id = id, nama = nama))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            // Inisialisasi otomatis jika file MyMultiUserPrefs.xml belum pernah dibuat
            listData.add(DataItem(id = "1", nama = "Ahmad Fauzi (Sampel 1)"))
            listData.add(DataItem(id = "2", nama = "Siti Nurhaliza (Sampel 2)"))
            saveDataToPreferences()
        }

        adapter.notifyDataSetChanged()
        updateUIState()
    }

    /**
     * Menyimpan seluruh daftar data ke file XML SharedPreferences (format JSON Array)
     */
    private fun saveDataToPreferences() {
        try {
            val jsonArray = JSONArray()
            for (item in listData) {
                val obj = JSONObject()
                obj.put("id", item.id)
                obj.put("nama", item.nama)
                jsonArray.put(obj)
            }
            // Simpan secara asynchronous ke file XML
            sharedPreferences.edit()
                .putString(KEY_LIST_DATA, jsonArray.toString())
                .apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Logika untuk Tambah Data Baru atau Simpan Perubahan Data (Update)
     */
    private fun simpanAtauUpdateData() {
        val input = etInputData.text.toString().trim()

        if (input.isEmpty()) {
            Toast.makeText(this, "Silakan isi data terlebih dahulu!", Toast.LENGTH_SHORT).show()
            return
        }

        if (editingPosition == -1) {
            // MODE TAMBAH (CREATE)
            val newItem = DataItem(nama = input)
            listData.add(newItem)

            // 1. Simpan ke file XML SharedPreferences
            saveDataToPreferences()

            // 2. Sinkronkan ke RecyclerView secara real-time
            val insertedIndex = listData.size - 1
            adapter.notifyItemInserted(insertedIndex)
            rvData.scrollToPosition(insertedIndex)

            Toast.makeText(this, "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
        } else {
            // MODE UBAH (UPDATE)
            editingItem?.let { item ->
                item.nama = input

                // 1. Simpan perubahan ke file XML SharedPreferences
                saveDataToPreferences()

                // 2. Sinkronkan item yang diedit ke RecyclerView secara real-time
                adapter.notifyItemChanged(editingPosition)

                Toast.makeText(this, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            }
            resetFormState()
        }

        // Reset input text & keyboard
        etInputData.text.clear()
        hideKeyboard()
        updateUIState()
    }

    /**
     * Memulai alur Ubah Data: mengisi form dan mengaktifkan mode Edit
     */
    private fun mulaiEditData(item: DataItem, position: Int) {
        editingPosition = position
        editingItem = item

        tvFormTitle.text = "Form Ubah Data (Baris ${position + 1})"
        etInputData.setText(item.nama)
        etInputData.requestFocus()
        etInputData.setSelection(item.nama.length)

        btnSimpanData.text = "Update Data"
        btnBatalEdit.visibility = View.VISIBLE
    }

    /**
     * Menampilkan dialog konfirmasi sebelum menghapus data (DELETE)
     */
    private fun konfirmasiHapusData(item: DataItem, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Data")
            .setMessage("Apakah Anda yakin ingin menghapus \"${item.nama}\"?")
            .setPositiveButton("Hapus") { dialog, _ ->
                // Jika data yang dihapus sedang dalam form edit, batalkan edit terlebih dahulu
                if (editingPosition == position) {
                    resetFormState()
                }

                // 1. Hapus dari list
                listData.removeAt(position)

                // 2. Simpan list terbaru ke file XML SharedPreferences
                saveDataToPreferences()

                // 3. Sinkronkan penghapusan ke RecyclerView secara real-time
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, listData.size - position)

                updateUIState()
                Toast.makeText(this, "Data berhasil dihapus!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    /**
     * Mengembalikan form ke mode tambah normal
     */
    private fun resetFormState() {
        editingPosition = -1
        editingItem = null
        tvFormTitle.text = "Form Tambah Data"
        btnSimpanData.text = "Tambah Data"
        btnBatalEdit.visibility = View.GONE
        etInputData.text.clear()
        hideKeyboard()
    }

    /**
     * Memperbarui informasi total data dan tampilan status kosong
     */
    private fun updateUIState() {
        tvTotalData.text = "Total Data: ${listData.size}"
        if (listData.isEmpty()) {
            tvKosong.visibility = View.VISIBLE
            rvData.visibility = View.GONE
        } else {
            tvKosong.visibility = View.GONE
            rvData.visibility = View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}