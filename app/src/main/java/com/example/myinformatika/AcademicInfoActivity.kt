package com.example.myinformatika

import android.content.Intent
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
import com.google.android.material.bottomnavigation.BottomNavigationView

class AcademicInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_academic_info)

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

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_info

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
                    val userType = sharedPref.getString("USER_TYPE", "Mahasiswa")
                    val homeIntent = if (userType == "Admin") {
                        Intent(this, AdminHomeActivity::class.java)
                    } else {
                        Intent(this, HomeActivity::class.java)
                    }
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(homeIntent)
                    finish()
                    true
                }
                R.id.navigation_article -> {
                    val intent = Intent(this, HomeArticleActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.navigation_info -> {
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        // RecyclerView dan data
        val recyclerView: RecyclerView = findViewById(R.id.recyclerAcademicInfo)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dataList = listOf(
            InfoItemAcademic(
                category = "Akademik",
                title = "KRS Online",
                description = "Pengisian KRS semester ganjil periode 2025 akan dimulai pada tanggal 17 Agustus 2025 hingga 26 Agustus 2025.",
                date = "2025-06-26",
                views = "0"
            ),
            InfoItemAcademic(
                category = "Beasiswa",
                title = "Pendaftaran Beasiswa",
                description = "Bagi mahasiswa semester 2-6 yang ingin mendaftarkan diri pada beasiswa mahasiswa berprestasi yang diselenggarakan oleh PT Azkiya Maju bisa segera ke prodi",
                date = "2025-06-26",
                views = "0"
            ),
            InfoItemAcademic(
                category = "Kuliah Semester Pendek",
                title = "Pendaftaran KSP",
                description = "Pendaftaran Kuliah Semester Pendek (KSP) akan dibuka pada tanggal 1 Juli 2025 sampai 7 Juli 2025",
                date = "2025-06-26",
                views = "0"
            )
        )
        recyclerView.adapter = InfoAdapter(dataList)
    }
}
