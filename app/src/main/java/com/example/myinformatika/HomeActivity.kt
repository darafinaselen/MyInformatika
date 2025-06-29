package com.example.myinformatika

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import java.util.Calendar
import android.content.Intent
import android.util.Log
import com.google.android.material.card.MaterialCardView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val tvGreeting: TextView = findViewById(R.id.tvGreeting)
        val tvWelcomeMessage: TextView = findViewById(R.id.tvWelcomeMessage)
        val ivUserProfileHome: CircleImageView = findViewById(R.id.ivUserProfileHome)

        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val fullName = sharedPref.getString("FULL_NAME", "Pengguna")
        val profileImageUrl = sharedPref.getString("PROFILE_IMAGE_URL", null)
        val userType = sharedPref.getString("USER_TYPE", "Mahasiswa")

        Log.d("HOME_DEBUG", "URL dari SharedPreferences: $profileImageUrl")

        tvGreeting.text = getGreetingMessage()
        tvWelcomeMessage.text = fullName

        if (profileImageUrl != null) {
            val imageFile = File(profileImageUrl)
            Glide.with(this)
                .load(imageFile)
                .placeholder(R.drawable.baseline_account_circle_24)
                .error(R.drawable.baseline_account_circle_24)
                .into(ivUserProfileHome)
        }

        val btnFeedback: MaterialCardView = findViewById(R.id.btnFeedback)
        btnFeedback.setOnClickListener {
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
        }

        val btnLabInfo: MaterialCardView = findViewById(R.id.btnLabInfo)
        btnLabInfo.setOnClickListener {
            val intent = Intent(this, LabInfoActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_home
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
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
//                    startActivity(Intent(this, InfoActivity::class.java))
//                    overridePendingTransition(0, 0)
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

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val rvDynamicInfo: RecyclerView = findViewById(R.id.rvDynamicInfo)

        // Siapkan data dummy dengan gambar dan viewType
        val dummyInfoList = listOf(
            InfoItem(
                title = "[Info Perkuliahan Semester Ganjil 2023 - 2024]",
                date = "March 01, 2021",
                viewType = 1 // Tipe 1 untuk item utama
            ),
            InfoItem(
                title = "Prospek Kerja Lulusan Teknik Informatika dan Kisaran gajinya",
                date = "Sunday, 10 September 2023",
                viewType = 1
            ),
            InfoItem(
                title = "Jadwal Praktikum Pemrograman Mobile",
                date = "Monday, 11 September 2023",
                viewType = 1
            )
        )

        val infoAdapter = DynamicInfoAdapter(dummyInfoList)
        rvDynamicInfo.adapter = infoAdapter
        rvDynamicInfo.layoutManager = LinearLayoutManager(this)
    }

    private fun getGreetingMessage(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 4..11 -> "Good Morning!" // Jam 4 Pagi - 11 Siang
            in 12..16 -> "Good Afternoon!" // Jam 12 Siang - 4 Sore
            in 17..20 -> "Good Evening!" // Jam 5 Sore - 8 Malam
            else -> "Good Night!" // Jam 9 Malam - 3 Pagi
        }
    }
}