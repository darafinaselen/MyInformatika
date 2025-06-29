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
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val tvGreeting: TextView = findViewById(R.id.tvGreeting)
        val tvWelcomeMessage: TextView = findViewById(R.id.tvWelcomeMessage)
        val ivUserProfileHome: CircleImageView = findViewById(R.id.ivUserProfileHome)

        val userType = intent.getStringExtra("USER_TYPE")
        val fullName = intent.getStringExtra("FULL_NAME")
        val profileImageUrl = intent.getStringExtra("PROFILE_IMAGE_URL")

        tvGreeting.text = getGreetingMessage()
        tvWelcomeMessage.text = fullName
        Glide.with(this).load(profileImageUrl).placeholder(R.drawable.baseline_account_circle_24).error(R.drawable.baseline_account_circle_24).into(ivUserProfileHome)

        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Sudah di main/home
                    true
                }
                R.id.navigation_article -> {
                    startActivity(Intent(this, HomeArticleActivity::class.java))
                    true
                }
                else -> false
            }
        }
        val btnLabInfo: MaterialCardView = findViewById(R.id.btnLabInfo)
        btnLabInfo.setOnClickListener {
            val intent = Intent(this, LabInfoActivity::class.java)
            startActivity(intent)
        }
//        val btnFeedback: MaterialCardView = findViewById(R.id.btnFeedback)
//        btnFeedback.setOnClickListener {
//            val intent = Intent(this, FeedbackActivity::class.java)
//            startActivity(intent)
//        }

//        val adminPanelView: View = findViewById(R.id.adminPanel)
//        if (userType == "Admin") {
//            adminPanelView.visibility = View.VISIBLE
//        } else {
//            adminPanelView.visibility = View.GONE
//        }
//
//        setupRecyclerView()
    }

//    private fun setupRecyclerView() {
//        val rvDynamicInfo: RecyclerView = findViewById(R.id.rvDynamicInfo)
//
//        val dummyInfoList = listOf(
//            InfoItem("Info Perkuliahan Semester Ganjil 2023 - 2024", "March 01, 2021"),
//            InfoItem("Prospek Kerja Lulusan Teknik Informatika", "Sunday, 10 September 2023"),
//            InfoItem("Jadwal Praktikum Pemrograman Mobilen ", "Monday, 11 September 2023")
//        )
//
//        val infoAdapter = DynamicInfoAdapter(dummyInfoList)
//        rvDynamicInfo.adapter = infoAdapter
//        rvDynamicInfo.layoutManager = LinearLayoutManager(this)
//        rvDynamicInfo.isNestedScrollingEnabled = false
//    }


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