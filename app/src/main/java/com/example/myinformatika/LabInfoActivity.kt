package com.example.myinformatika

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LabInfoActivity : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var backgroundImage: ImageView
    private lateinit var dropdownButton: TextView
    private lateinit var dropdownList: LinearLayout
    private lateinit var titleText: TextView
    private lateinit var desc1: TextView
    private lateinit var desc2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_labinformation)
        initViews()

        val initialLabNumber = intent.getIntExtra("LAB_NUMBER_EXTRA", 1)
        updateContentForLab(initialLabNumber)
        setupClickListeners()
    }

    private fun initViews() {
        backButton = findViewById(R.id.backButtonLabinfor)
        backgroundImage = findViewById(R.id.backgroundImageLabinfor)
        dropdownButton = findViewById(R.id.dropdownButtonLabinfor)
        dropdownList = findViewById(R.id.dropdownListLabinfor)
        titleText = findViewById(R.id.titleTextLabinfor)
        desc1 = findViewById(R.id.descriptionTextLabinfor)
        desc2 = findViewById(R.id.descriptionTextSecondLabinfor)
    }

    private fun setupClickListeners() {
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            startActivity(intent)
        }
        dropdownButton.setOnClickListener {
            if (dropdownList.visibility == View.VISIBLE) {
                dropdownList.visibility = View.GONE
            } else {
                dropdownList.visibility = View.VISIBLE
            }
        }

        findViewById<TextView>(R.id.itemLab1).setOnClickListener {
            updateContentForLab(1)
        }
        findViewById<TextView>(R.id.itemLab2).setOnClickListener {
            updateContentForLab(2)
        }
        findViewById<TextView>(R.id.itemLab3).setOnClickListener {
            updateContentForLab(3)
        }
        findViewById<TextView>(R.id.itemLab4).setOnClickListener {
            updateContentForLab(4)
        }
    }

    // Fungsi utama untuk mengubah konten sesuai nomor lab yang dipilih
    private fun updateContentForLab(labNumber: Int) {
        when (labNumber) {
            1 -> {
                backgroundImage.setImageResource(R.drawable.lab1)
                titleText.text = "Laboratorium Sistem Cerdas"
                desc1.text = "Laboratorium Sistem Cerdas dan Aplikasinya berorientasi pada pengembangan metodologi penalaran komputer, khususnya pengembangan aspek kecerdasan buatan..."
                desc2.text = "Bidang penelitian laboratorium ini meliputi semua tahapan pengembangan perangkat lunak..."
                dropdownButton.text = "Laboratorium 1" // Update teks tombol dropdown
            }
            2 -> {
                backgroundImage.setImageResource(R.drawable.lab2)
                titleText.text = "Laboratorium Komunikasi Data dan Sistem Tertanam"
                desc1.text = "Laboratorium ini mengkaji protokol komunikasi, keamanan data, transmisi nirkabel, dan teknologi terkait untuk meningkatkan efisiensi dan keamanan jaringan. Di sisi lain, dalam sistem tertanam, laboratorium ini berfokus pada pengembangan perangkat keras dan perangkat lunak yang tertanam dalam perangkat elektronik, termasuk mikrokontroler, sistem kendali otomatis, dan keamanan sistem tertanam. Laboratorium ini digunakan oleh para peneliti, insinyur, dan mahasiswa untuk mengembangkan dan menguji teknologi terkait komunikasi data dan sistem tertanam."
                desc2.visibility = View.GONE // Sembunyikan jika tidak ada deskripsi kedua
                dropdownButton.text = "Laboratorium 2"
            }
            3 -> {
                backgroundImage.setImageResource(R.drawable.lab3)
                titleText.text = "Laboratorium Sistem Informasi Enterprise"
                desc1.text = "Laboratorium SI-Enterpise mengintegrasikan teknologi informasi dengan proses bisnis organisasi untuk meningkatkan efisiensi dan produktivitas. Laboratorium ini mencakup studi tentang perangkat lunak manajemen perusahaan, analisis data besar, keamanan informasi, dan strategi teknologi informasi yang berfokus pada kebutuhan perusahaan. Melalui penelitian dan eksperimen di laboratorium ini, pemahaman yang lebih baik tentang cara menggunakan teknologi informasi untuk mendukung operasi perusahaan dan pengambilan keputusan dapat ditemukan dan diterapkan."
                desc2.visibility = View.GONE
                dropdownButton.text = "Laboratorium 3"
            }
            4 -> {
                backgroundImage.setImageResource(R.drawable.lab4)
                titleText.text = "Komputer Dasar"
                desc1.text = "Laboratorium Komputer Dasar menyediakan fasilitas pembelajaran bagi mahasiswa untuk mempelajari dan mempraktikkan keterampilan dasar teknologi informasi. Materi yang dipelajari meliputi pengenalan perangkat keras dan perangkat lunak, penggunaan sistem operasi, pengolah kata, lembar kerja, presentasi, serta dasar-dasar Browse dan komunikasi digital."
                desc2.visibility = View.GONE
                dropdownButton.text = "Laboratorium 4"
            }
        }
        dropdownList.visibility = View.GONE
        if(labNumber == 1) desc2.visibility = View.VISIBLE
    }
}