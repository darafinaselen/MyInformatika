package com.example.myinformatika

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var profileImage: CircleImageView
    private lateinit var tvFullname: TextView
    private lateinit var tvStudentNumber: TextView
    private lateinit var tvEntryDate: TextView
    private lateinit var tvUserType: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvPhoneNumber: TextView
    private lateinit var tvEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Inisialisasi semua View
        profileImage = findViewById(R.id.profileImage)
        tvFullname = findViewById(R.id.tvFullname)
        tvStudentNumber = findViewById(R.id.tvStudentNumber)
        tvEntryDate = findViewById(R.id.tvEntryDate)
        tvUserType = findViewById(R.id.tvUserType)
        tvGender = findViewById(R.id.tvGender)
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber)
        tvEmail = findViewById(R.id.tvEmail)

        findViewById<LinearLayout>(R.id.editProfileButton).setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.logoutButton).setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_profile

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
                    finish() // Tutup halaman ini
                    true
                }
                R.id.navigation_info -> {
                    // Jika InfoActivity sudah ada, aktifkan ini
//                     val intent = Intent(this, InfoActivity::class.java)
                     intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                     startActivity(intent)
                     finish()
                    true
                }
                R.id.navigation_profile -> {
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val user = auth.currentUser
        if (user != null) {
            val uid = user.uid
            val userRef = db.collection("users").document(uid)

            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        Log.d("FirestoreData", "Document data: ${document.data}")

                        val fullname = document.getString("fullName")
                        val studentNumber = document.getString("idNumber")
                        val entryDate = document.getString("entryDate")
                        val userType = document.getString("userType")
                        val gender = document.getString("gender")
                        val phoneNumber = document.getString("phoneNumber")
                        val email = document.getString("email")
                        val profileImageUrl = document.getString("profileImageUrl")

                        // Tampilkan data
                        tvFullname.text = fullname ?: "Data tidak ada"
                        tvStudentNumber.text = studentNumber ?: "Data tidak ada"
                        tvEntryDate.text = entryDate ?: "Data tidak ada"
                        tvUserType.text = userType ?: "Data tidak ada"
                        tvGender.text = gender ?: "Data tidak ada"
                        tvPhoneNumber.text = phoneNumber ?: "Data tidak ada"
                        tvEmail.text = email ?: "Data tidak ada"

                        // Muat gambar profil
                        if (!profileImageUrl.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.profile)
                                .error(R.drawable.profile) // Tampilkan gambar default jika error
                                .into(profileImage)
                        } else {
                            Log.d("FirestoreData", "URL gambar profil kosong atau tidak ada.")
                        }

                    } else {
                        Log.d("ProfileActivity", "Dokumen profil tidak ditemukan.")
                        Toast.makeText(this, "Data profil tidak ditemukan.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("ProfileActivity", "Error getting documents.", exception)
                    Toast.makeText(this, "Gagal memuat data.", Toast.LENGTH_SHORT).show()
                }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
