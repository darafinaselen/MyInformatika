package com.example.myinformatika

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class AdminHomeActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var rvDynamicInfo: RecyclerView
    private lateinit var tvFeedbackCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
        db = FirebaseFirestore.getInstance()

        rvDynamicInfo = findViewById(R.id.rvDynamicInfo)
        tvFeedbackCount = findViewById(R.id.tvFeedbackCount)
        val tvGreeting: TextView = findViewById(R.id.tvGreeting)
        val tvWelcomeMessage: TextView = findViewById(R.id.tvWelcomeMessage)
        val ivUserProfileHome: CircleImageView = findViewById(R.id.ivUserProfileHome)
        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val fullName = sharedPref.getString("FULL_NAME", "Pengguna")
        val profileImageUrl = sharedPref.getString("PROFILE_IMAGE_URL", null)
        val userType = sharedPref.getString("USER_TYPE", "Mahasiswa")

        tvGreeting.text = getGreetingMessage()
        tvWelcomeMessage.text = fullName
        if (!profileImageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(profileImageUrl)
                .placeholder(R.drawable.baseline_account_circle_24)
                .error(R.drawable.baseline_account_circle_24)
                .into(ivUserProfileHome)
        } else {
            ivUserProfileHome.setImageResource(R.drawable.baseline_account_circle_24)
        }

        val btnFeedback: MaterialCardView = findViewById(R.id.btnFeedback)
        btnFeedback.setOnClickListener {
            val intent = Intent(this, AdminFeedbackActivity::class.java)
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
                    val intent = Intent(this, AcademicInfoActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
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

        loadUnseenFeedback()
    }
    private fun loadUnseenFeedback() {
        db.collection("feedback")
            .whereEqualTo("isDone", false)
            .get()
            .addOnSuccessListener { documents ->
                val totalCount = documents.size()
                if (totalCount > 0) {
                    tvFeedbackCount.visibility = View.VISIBLE
                    tvFeedbackCount.text = if (totalCount > 9) "9+" else totalCount.toString()
                } else {
                    tvFeedbackCount.visibility = View.GONE
                }
            }
            .addOnFailureListener { e ->
                Log.w("AdminHome", "Gagal menghitung total feedback", e)
                tvFeedbackCount.visibility = View.GONE
            }

        // Query untuk mengambil 3 feedback TERATAS yang belum selesai
        db.collection("feedback")
            .whereEqualTo("isDone", false)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(3)
            .get()
            .addOnSuccessListener { documents ->
                val feedbackList = documents.toObjects(FeedbackItem::class.java)
                setupRecyclerView(feedbackList)
            }
            .addOnFailureListener { exception ->
                Log.e("AdminHome", "Error getting feedback list: ", exception)
                Toast.makeText(this, "Gagal memuat feedback. Cek Logcat untuk error.", Toast.LENGTH_LONG).show()
            }
    }
    private fun setupRecyclerView(feedbackList: List<FeedbackItem>) {
        rvDynamicInfo.layoutManager = LinearLayoutManager(this)
        rvDynamicInfo.adapter = HomeFeedbackAdapter(feedbackList)
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