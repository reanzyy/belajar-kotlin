package com.example.logisticstrackerapp

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var editTextTrackingNumber: EditText
    private lateinit var buttonTrack: Button
    private lateinit var progressBarLoading: ProgressBar
    private lateinit var cardResult: CardView
    private lateinit var cardErrorContainer: MaterialCardView
    private lateinit var textViewErrorMessage: TextView

    // Detail Resi
    private lateinit var textViewResiTitle: TextView
    private lateinit var textViewStatusBadge: TextView
    private lateinit var textViewCourier: TextView
    private lateinit var textViewLocation: TextView
    private lateinit var textViewRecipient: TextView
    private lateinit var textViewEta: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi Views
        editTextTrackingNumber = findViewById(R.id.editTextTrackingNumber)
        buttonTrack = findViewById(R.id.buttonTrack)
        progressBarLoading = findViewById(R.id.progressBarLoading)
        cardResult = findViewById(R.id.cardResult)
        cardErrorContainer = findViewById(R.id.cardErrorContainer)
        textViewErrorMessage = findViewById(R.id.textViewErrorMessage)

        textViewResiTitle = findViewById(R.id.textViewResiTitle)
        textViewStatusBadge = findViewById(R.id.textViewStatusBadge)
        textViewCourier = findViewById(R.id.textViewCourier)
        textViewLocation = findViewById(R.id.textViewLocation)
        textViewRecipient = findViewById(R.id.textViewRecipient)
        textViewEta = findViewById(R.id.textViewEta)

        // Tombol Quick Select Resi
        val quickResiIds = listOf(
            R.id.btnQuickResi1 to "EXP-8801",
            R.id.btnQuickResi2 to "EXP-8802",
            R.id.btnQuickResi3 to "EXP-8803",
            R.id.btnQuickResi4 to "EXP-8804",
            R.id.btnQuickResi5 to "EXP-8805"
        )

        quickResiIds.forEach { (buttonId, resiValue) ->
            findViewById<Button>(buttonId).setOnClickListener {
                editTextTrackingNumber.setText(resiValue)
                editTextTrackingNumber.setSelection(resiValue.length)
                cekDanLacak(resiValue)
            }
        }

        // Tombol Lacak Utama
        buttonTrack.setOnClickListener {
            val nomorResi = editTextTrackingNumber.text.toString().trim()
            if (nomorResi.isNotEmpty()) {
                cekDanLacak(nomorResi)
            } else {
                Toast.makeText(this, "Silakan masukkan nomor resi terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cekDanLacak(nomorResi: String) {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Perangkat Offline - Periksa Sambungan Jaringan Anda", Toast.LENGTH_LONG).show()
            return
        }
        lacakPaket(nomorResi)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun lacakPaket(nomorResi: String) {
        progressBarLoading.visibility = View.VISIBLE
        cardResult.visibility = View.GONE
        cardErrorContainer.visibility = View.GONE
        buttonTrack.isEnabled = false

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.getTrackingDetail(nomorResi)

                withContext(Dispatchers.Main) {
                    progressBarLoading.visibility = View.GONE
                    buttonTrack.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        tampilkanDataPaket(response.body()!!)
                    } else {
                        textViewErrorMessage.text = "Nomor resi [$nomorResi] tidak ditemukan pada database server (HTTP ${response.code()})"
                        cardErrorContainer.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBarLoading.visibility = View.GONE
                    buttonTrack.isEnabled = true
                    textViewErrorMessage.text = "Koneksi internet bermasalah: ${e.localizedMessage ?: "Gagal terhubung ke server"}"
                    cardErrorContainer.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun tampilkanDataPaket(dataPaket: TrackingResponse) {
        cardResult.visibility = View.VISIBLE
        textViewResiTitle.text = "Resi: ${dataPaket.trackingNumber}"
        textViewCourier.text = "Kurir: ${dataPaket.serviceType} (${dataPaket.courierName})"
        textViewLocation.text = "Posisi Terakhir: ${dataPaket.lastLocation}"
        textViewRecipient.text = "Penerima: ${dataPaket.recipientName}"
        textViewEta.text = "Estimasi Tiba: ${dataPaket.estimatedDelivery}"

        textViewStatusBadge.text = dataPaket.status
        val badgeBackground = textViewStatusBadge.background as? GradientDrawable ?: GradientDrawable()
        badgeBackground.cornerRadius = 30f

        when (dataPaket.status.uppercase()) {
            "DELIVERED" -> {
                badgeBackground.setColor(Color.parseColor("#DCFCE7"))
                textViewStatusBadge.background = badgeBackground
                textViewStatusBadge.setTextColor(Color.parseColor("#158030"))
            }
            "IN_TRANSIT" -> {
                badgeBackground.setColor(Color.parseColor("#FEF3C7"))
                textViewStatusBadge.background = badgeBackground
                textViewStatusBadge.setTextColor(Color.parseColor("#B45309"))
            }
            else -> {
                badgeBackground.setColor(Color.parseColor("#F1F5F9"))
                textViewStatusBadge.background = badgeBackground
                textViewStatusBadge.setTextColor(Color.parseColor("#475569"))
            }
        }
    }
}