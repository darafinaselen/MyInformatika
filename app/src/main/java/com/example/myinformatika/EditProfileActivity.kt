package com.example.myinformatika

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {

    // ... (Deklarasi variabel tetap sama)
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var profileImageView: CircleImageView
    private lateinit var cameraIcon: ImageView
    private lateinit var etFullname: EditText
    private lateinit var etStudentNumber: EditText
    private lateinit var etEntryDate: EditText
    private lateinit var acUserType: AutoCompleteTextView
    private lateinit var etGender: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etEmail: EditText
    private lateinit var submitButton: Button
    private lateinit var backButton: ImageButton
    private var newImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let {
                newImageUri = it
                profileImageView.setImageURI(newImageUri)
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // ... (Inisialisasi Firebase dan View tetap sama)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        profileImageView = findViewById(R.id.profileImageView)
        cameraIcon = findViewById(R.id.cameraIcon)
        etFullname = findViewById(R.id.etFullname)
        etStudentNumber = findViewById(R.id.etStudentNumber)
        etEntryDate = findViewById(R.id.etEntryDate)
        acUserType = findViewById(R.id.acUserType)
        etGender = findViewById(R.id.etGender)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etEmail = findViewById(R.id.etEmail)
        submitButton = findViewById(R.id.submitButton)
        backButton = findViewById(R.id.backButton)

        // Muat data dan setup listener
        loadUserData()
        setupDropdown()
        setupDatePicker() // [DITAMBAHKAN] Panggil fungsi setup kalender

        backButton.setOnClickListener { finish() }
        cameraIcon.setOnClickListener { openGallery() }
        submitButton.setOnClickListener { saveProfileChanges() }
    }

    // [DITAMBAHKAN] Fungsi untuk menampilkan dialog kalender
    private fun setupDatePicker() {
        etEntryDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    etEntryDate.setText(dateFormat.format(selectedDate.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }

    // ... (Fungsi-fungsi lain tetap sama)
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    etFullname.setText(document.getString("fullName"))
                    etStudentNumber.setText(document.getString("idNumber"))
                    etEntryDate.setText(document.getString("entryDate"))
                    acUserType.setText(document.getString("userType"), false)
                    etGender.setText(document.getString("gender"))
                    etPhoneNumber.setText(document.getString("phoneNumber"))
                    etEmail.setText(document.getString("email"))

                    val imageUrl = document.getString("profileImageUrl")
                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(this).load(imageUrl).into(profileImageView)
                    }
                }
            }
    }

    private fun saveProfileChanges() {
        submitButton.isEnabled = false
        Toast.makeText(this, "Menyimpan perubahan...", Toast.LENGTH_SHORT).show()
        if (newImageUri != null) {
            uploadImageAndUpdateFirestore()
        } else {
            updateFirestoreData(null)
        }
    }

    private fun uploadImageAndUpdateFirestore() {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference.child("profile_images/$userId.jpg")
        newImageUri?.let { uri ->
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        updateFirestoreData(downloadUrl.toString())
                    }
                }
                .addOnFailureListener {
                    submitButton.isEnabled = true
                    Toast.makeText(this, "Gagal mengunggah foto.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateFirestoreData(newImageUrl: String?) {
        val userId = auth.currentUser?.uid ?: return
        val updatedData = mutableMapOf<String, Any>()
        updatedData["fullName"] = etFullname.text.toString()
        updatedData["idNumber"] = etStudentNumber.text.toString()
        updatedData["entryDate"] = etEntryDate.text.toString()
        updatedData["userType"] = acUserType.text.toString()
        updatedData["gender"] = etGender.text.toString()
        updatedData["phoneNumber"] = etPhoneNumber.text.toString()
        updatedData["email"] = etEmail.text.toString()
        if (newImageUrl != null) {
            updatedData["profileImageUrl"] = newImageUrl
        }
        db.collection("users").document(userId).update(updatedData)
            .addOnSuccessListener {
                Toast.makeText(this, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                submitButton.isEnabled = true
                Toast.makeText(this, "Gagal memperbarui profil.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupDropdown() {
        val userTypes = resources.getStringArray(R.array.user_types)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, userTypes)
        acUserType.setAdapter(adapter)
        acUserType.setOnClickListener {
            acUserType.showDropDown()
        }
    }
}
