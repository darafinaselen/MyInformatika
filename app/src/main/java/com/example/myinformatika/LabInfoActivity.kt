package com.example.myinformatika

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LabInfoActivity : AppCompatActivity() {
    private var isDropdownVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_labinformation)

        val backButton = findViewById<ImageButton>(R.id.backButtonLabinfor)
        val dropdownButton = findViewById<TextView>(R.id.dropdownButtonLabinfor)
        val dropdownList = findViewById<LinearLayout>(R.id.dropdownListLabinfor)

        val itemLab1 = findViewById<TextView>(R.id.itemLab1)
        val itemLab2 = findViewById<TextView>(R.id.itemLab2)
        val itemLab3 = findViewById<TextView>(R.id.itemLab3)
        val itemLab4 = findViewById<TextView>(R.id.itemLab4)

        val titleText = findViewById<TextView>(R.id.titleTextLabinfor)
        val desc1 = findViewById<TextView>(R.id.descriptionTextLabinfor)
        val desc2 = findViewById<TextView>(R.id.descriptionTextSecondLabinfor)

//        backButton.setOnClickListener {
//            val intent = Intent(this, HomeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

        dropdownButton.setOnClickListener {
            isDropdownVisible = !isDropdownVisible
            dropdownList.visibility = if (isDropdownVisible) View.VISIBLE else View.GONE
        }

        val updateContent = { labNumber: Int ->
            dropdownButton.text = "Laboratorium $labNumber"
            dropdownList.visibility = View.GONE
            isDropdownVisible = false

            // Ganti isi sesuai lab
            when (labNumber) {
                1 -> {
                    titleText.text = "Laboratorium Sistem Cerdas"
                    desc1.text = "Laboratorium Sistem Cerdas dan Aplikasinya berorientasi pada pengembangan metodologi penalaran komputer, khususnya pengembangan aspek kecerdasan buatan..."
                    desc2.text = "Bidang penelitian laboratorium ini meliputi semua tahapan pengembangan perangkat lunak..."
                }
                2 -> {
                    titleText.text = "Laboratorium Komunikasi Data dan\n" +
                            "Sistem Tertanam"
                    desc1.text = "Laboratorium ini mengkaji protokol komunikasi, keamanan data, transmisi nirkabel, dan teknologi terkait untuk meningkatkan efisiensi dan keamanan jaringan. Di sisi lain, dalam sistem tertanam, laboratorium ini berfokus pada pengembangan perangkat keras dan perangkat lunak yang tertanam dalam perangkat elektronik, termasuk mikrokontroler, sistem kendali otomatis, dan keamanan sistem tertanam. Laboratorium ini digunakan oleh para peneliti, insinyur, dan mahasiswa untuk mengembangkan dan menguji teknologi terkait komunikasi data dan sistem tertanam."
                    desc2.text = ""
                }
                3 -> {
                    titleText.text = "Laboratorium Sistem Informasi \n" +
                            "Enterprise"
                    desc1.text = "Laboratorium SI-Enterpise mengintegrasikan teknologi informasi dengan proses bisnis organisasi untuk meningkatkan efisiensi dan produktivitas. Laboratorium ini mencakup studi tentang perangkat lunak manajemen perusahaan, analisis data besar, keamanan informasi, dan strategi teknologi informasi yang berfokus pada kebutuhan perusahaan. Melalui penelitian dan eksperimen di laboratorium ini, pemahaman yang lebih baik tentang cara menggunakan teknologi informasi untuk mendukung operasi perusahaan dan pengambilan keputusan dapat ditemukan dan diterapkan."
                    desc2.text = ""
                }
                4 -> {
                    titleText.text = "Komputer Dasar"
                    desc1.text = "Laboratorium Komputer Dasar menyediakan fasilitas pembelajaran bagi mahasiswa untuk mempelajari dan mempraktikkan keterampilan dasar teknologi informasi. Materi yang dipelajari meliputi pengenalan perangkat keras dan perangkat lunak, penggunaan sistem operasi, pengolah kata, lembar kerja, presentasi, serta dasar-dasar browsing dan komunikasi digital."
                    desc2.text = ""
                }
            }
        }

        itemLab1.setOnClickListener { updateContent(1) }
        itemLab2.setOnClickListener { updateContent(2) }
        itemLab3.setOnClickListener { updateContent(3) }
        itemLab4.setOnClickListener { updateContent(4) }
    }
}
