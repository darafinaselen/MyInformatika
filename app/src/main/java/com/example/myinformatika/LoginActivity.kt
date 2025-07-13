package com.example.myinformatika

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
//import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var etIdentifier: TextInputEditText
    private lateinit var etPasswordLogin: TextInputEditText
    private lateinit var btnLogin: Button
//    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        if (auth.currentUser != null) {
//            val progressBarCheck: ProgressBar = findViewById(R.id.progressBarLogin)
//            progressBarCheck.visibility = View.VISIBLE
            fetchUserAndGoToHome(auth.currentUser!!.uid)
            return
        }
        etIdentifier = findViewById(R.id.etEmailLogin)
        etPasswordLogin = findViewById(R.id.etPasswordLogin)
        btnLogin = findViewById(R.id.btnLogin)
//        progressBar = findViewById(R.id.progressBarLogin)
        val loginFormContainer: LinearLayout = findViewById(R.id.loginFormContainer)

        Handler(Looper.getMainLooper()).postDelayed({
            loginFormContainer.visibility = View.VISIBLE
            loginFormContainer.animate()
                .translationY(0f)
                .setDuration(800)
                .start()
        }, 500)

        val tvCreateAccount: TextView = findViewById(R.id.tvCreateAccount)
        tvCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val identifier = etIdentifier.text.toString().trim()
            val password = etPasswordLogin.text.toString().trim()

            if (TextUtils.isEmpty(identifier) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Input tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginUser(identifier, password)
        }

        val tvForgotPassword: TextView = findViewById(R.id.tvForgotPassword)
        tvForgotPassword.setOnClickListener {
            val identifier = etIdentifier.text.toString().trim()
            if (TextUtils.isEmpty(identifier)) {
                Toast.makeText(this, "Silakan isi email atau ID Number Anda terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//            progressBar.visibility = View.VISIBLE
            sendPasswordReset(identifier)
        }
    }

    private fun loginUser(identifier: String, password: String) {
//        progressBar.visibility = View.VISIBLE
        btnLogin.isEnabled = false

        if (identifier.contains("@")) {
            performFirebaseLogin(identifier, password)
        } else {
            firestore.collection("users")
                .whereEqualTo("studentNumber", identifier)
                .limit(1)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        handleLoginFailure("ID Number tidak ditemukan.")
                    } else {
                        val userDocument = querySnapshot.documents[0]
                        val email = userDocument.getString("email")
                        if (email != null) {
                            performFirebaseLogin(email, password)
                        } else {
                            handleLoginFailure("Data email tidak ditemukan untuk ID ini.")
                        }
                    }
                }
                .addOnFailureListener {
                    handleLoginFailure("Gagal mencari data ID Number.")
                }
        }
    }
    private fun performFirebaseLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                fetchUserAndGoToHome(authResult.user!!.uid)
            }
            .addOnFailureListener { e ->
                handleLoginFailure("Login gagal: Email atau Password salah.")
            }
    }
    private fun fetchUserAndGoToHome(uid: String) {
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
//                progressBar.visibility = View.GONE
//                btnLogin.isEnabled = true
                if (document != null && document.exists()) {
                    val userType = document.getString("userType") ?: "Mahasiswa"
                    val fullName = document.getString("fullName") ?: "Pengguna Baru"
                    val profileImageUrl = document.getString("profileImageUrl") ?: ""
                    val email = document.getString("email") ?: ""
                    val idNumber = document.getString("idNumber") ?: ""
                    val phoneNumber = document.getString("phoneNumber") ?: ""
                    val gender = document.getString("gender") ?: ""
                    val entryDate = document.getString("entryDate") ?: ""

                    Log.d("LOGIN_DEBUG", "URL dari Firestore: $profileImageUrl")

                    val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("USER_TYPE", userType)
                        putString("FULL_NAME", fullName)
                        putString("PROFILE_IMAGE_URL", profileImageUrl)
                        putString("EMAIL", email)
                        putString("ID_NUMBER", idNumber)
                        putString("PHONE_NUMBER", phoneNumber)
                        putString("GENDER", gender)
                        putString("ENTRY_DATE", entryDate)
                        apply()
                    }

                    val intent = if (userType.equals("Admin", ignoreCase = true)) {
                        Intent(this, AdminHomeActivity::class.java)
                    } else {
                        Intent(this, HomeActivity::class.java)
                    }

                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()

                } else {
                    handleLoginFailure("Login berhasil tapi data profil tidak ditemukan.")
                }
            }
            .addOnFailureListener {
                handleLoginFailure("Gagal mengambil data profil.")
            }
    }
    private fun handleLoginFailure(message: String) {
//        progressBar.visibility = View.GONE
        btnLogin.isEnabled = true
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        if (auth.currentUser != null) {
            auth.signOut()
        }
    }
    private fun sendPasswordReset(identifier: String) {
        // Fungsi ini akan mencari email terlebih dahulu jika inputnya adalah ID Number
        val emailToReset: (String) -> Unit = { email ->
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
//                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Link untuk reset password telah dikirim ke $email", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
//                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Gagal mengirim link: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        if (identifier.contains("@")) {
            // Jika identifier adalah email
            emailToReset(identifier)
        } else {
            // Jika identifier adalah ID Number, cari emailnya di Firestore
            firestore.collection("users")
                .whereEqualTo("idNumber", identifier) // Pastikan nama field di Firestore benar
                .limit(1)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
//                        progressBar.visibility = View.GONE
                        Toast.makeText(this, "ID Number tidak ditemukan.", Toast.LENGTH_SHORT).show()
                    } else {
                        val userDocument = querySnapshot.documents[0]
                        val email = userDocument.getString("email")
                        if (email != null) {
                            emailToReset(email)
                        } else {
//                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Data email tidak ditemukan untuk ID ini.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener {
//                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Gagal mencari data ID Number.", Toast.LENGTH_SHORT).show()
                }
        }
    }
}