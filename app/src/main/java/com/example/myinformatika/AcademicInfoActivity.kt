package com.example.myinformatika

import android.os.Bundle
import android.os.Build
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AcademicInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.academic_info)

        // Status bar transparan
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                statusBarColor = Color.TRANSPARENT
                decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        )
            }
        }

        // RecyclerView dan data
        val recyclerView: RecyclerView = findViewById(R.id.recyclerAcademicInfo)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dataList = listOf(
            InfoItem(
                category = "Akademik",
                title = "KRS Online",
                description = "Pengisian KRS semester ganjil periode 2025 akan dimulai pada tanggal 17 Agustus 2025 hingga 26 Agustus 2025.",
                date = "2025-06-26",
                views = "0"
            ),
            InfoItem(
                category = "Beasiswa",
                title = "Pendaftaran Beasiswa",
                description = "Bagi mahasiswa semester 2-6 yang ingin mendaftarkan diri pada beasiswa mahasiswa berprestasi yang diselenggarakan oleh PT Azkiya Maju bisa segera ke prodi",
                date = "2025-06-26",
                views = "0"
            ),
            InfoItem(
                category = "Kuliah Semester Pendek",
                title = "Pendaftaran KSP",
                description = "Pendaftaran Kuliah Semester Pendek (KSP) akan dibuka pada tanggal 1 Juli 2025 sampai 7 Juli 2025",
                date = "2025-06-26",
                views = "0"
            )
        )
        recyclerView.adapter = InfoAdapter(
            dataList,
            onDeleteClick = TODO()
        )
    }
}
