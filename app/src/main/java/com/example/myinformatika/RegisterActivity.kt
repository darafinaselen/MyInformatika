package com.example.myinformatika

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    private lateinit var ivProfile: CircleImageView
    private lateinit var etFullname: TextInputEditText
    private lateinit var etIdNumber: TextInputEditText
    private lateinit var etEntryDate: TextInputEditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var etPhoneNumber: TextInputEditText
    private lateinit var etEmailRegister: TextInputEditText
    private lateinit var etPasswordRegister: TextInputEditText
    private lateinit var btnRegister: Button
//    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        storage = Firebase.storage

        ivProfile = findViewById(R.id.ivProfile)
        etFullname = findViewById(R.id.etFullname)
        etIdNumber = findViewById(R.id.etIdNumber)
        etEntryDate = findViewById(R.id.etEntryDate)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etEmailRegister = findViewById(R.id.etEmailRegister)
        etPasswordRegister = findViewById(R.id.etPasswordRegister)
        btnRegister = findViewById(R.id.btnRegister)

        setupImagePicker()
        setupDatePicker()

        btnRegister.setOnClickListener {
            registerUser()
        }
    }
    private fun setupImagePicker() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                imageUri = result.data?.data
                ivProfile.setImageURI(imageUri)
            }
        }
        ivProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intent)
        }
    }

    private fun setupDatePicker() {
        etEntryDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                etEntryDate.setText(dateFormat.format(selectedDate.time))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }
    }
    private fun registerUser() {
        val fullname = etFullname.text.toString().trim()
        val idNumber = etIdNumber.text.toString().trim()
        val entryDate = etEntryDate.text.toString().trim()
        val phoneNumber = etPhoneNumber.text.toString().trim()
        val email = etEmailRegister.text.toString().trim()
        val password = etPasswordRegister.text.toString().trim()
        val selectedGenderId = radioGroupGender.checkedRadioButtonId

        // --- Validasi Input ---
        if (imageUri == null) {
            Toast.makeText(this, "Silakan pilih foto profil", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(fullname) || TextUtils.isEmpty(idNumber) || TextUtils.isEmpty(entryDate) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || selectedGenderId == -1) {
            Toast.makeText(this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        btnRegister.isEnabled = false
        // Tampilkan ProgressBar di sini jika ada
        // progressBar.visibility = View.VISIBLE

        val selectedRadioButton = findViewById<RadioButton>(selectedGenderId)
        val gender = selectedRadioButton.text.toString()
        uploadImageAndSaveUserData(imageUri!!, fullname, idNumber, entryDate, "Student", gender, phoneNumber, email, password)
    }
    private fun uploadImageAndSaveUserData(
        imageUri: Uri, fullname: String, idNumber: String, entryDate: String, userType: String,
        gender: String, phoneNumber: String, email: String, password: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    val userId = auth.currentUser!!.uid
                    val storageRef = storage.reference.child("profile_images/$userId.jpg")

                    // Mulai proses upload file
                    storageRef.putFile(imageUri)
                        .continueWithTask { uploadTask ->
                            if (!uploadTask.isSuccessful) {
                                uploadTask.exception?.let { throw it }
                            }
                            // Jika upload berhasil, lanjutkan dengan mengambil URL download
                            storageRef.downloadUrl
                        }
                        .addOnCompleteListener { urlTask ->
                            if (urlTask.isSuccessful) {
                                val downloadUrl = urlTask.result.toString()
                                saveUserDataToFirestore(userId, downloadUrl, fullname, idNumber, entryDate, userType, gender, phoneNumber, email)
                            } else {
                                handleRegistrationFailure("Gagal mendapatkan URL gambar: ${urlTask.exception?.message}")
                            }
                        }
                } else {
                    // Gagal membuat akun Auth
                    handleRegistrationFailure("Registrasi gagal: ${authTask.exception?.message}")
                }
            }
    }

    private fun saveUserDataToFirestore(userId: String, profileImagePath: String, fullname: String, idNumber: String,
                                        entryDate: String, userType: String, gender: String, phoneNumber: String, email: String) {

        val userProfile = hashMapOf(
            "uid" to userId,
            "profileImageUrl" to profileImagePath,
            "fullName" to fullname,
            "idNumber" to idNumber,
            "entryDate" to entryDate,
            "userType" to userType,
            "gender" to gender,
            "phoneNumber" to phoneNumber,
            "email" to email,
            "createdAt" to System.currentTimeMillis()
        )

        firestore.collection("users").document(userId).set(userProfile)
            .addOnSuccessListener {
                // Sembunyikan loading dan tampilkan pesan sukses
                // progressBar.visibility = View.GONE
                btnRegister.isEnabled = true
                Toast.makeText(this, "Registrasi berhasil.", Toast.LENGTH_LONG).show()

                // Arahkan ke halaman Login atau Halaman Utama dan tutup activity ini
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                handleRegistrationFailure("Gagal menyimpan data profil: ${e.message}")
            }
    }

    private fun handleRegistrationFailure(message: String) {
//        progressBar.visibility = View.GONE
        btnRegister.isEnabled = true
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }

}