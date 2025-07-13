package com.example.myinformatika

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import android.widget.RadioButton
import android.widget.RadioGroup
//import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FeedbackActivity : AppCompatActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var etTanggal: EditText
    private lateinit var rgCategory: RadioGroup
    private lateinit var etFeedback: EditText
    private lateinit var btnSubmit: MaterialButton
//    private lateinit var progressBar: ProgressBar

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        ivBack = findViewById(R.id.ivBack)
        etTanggal = findViewById(R.id.etTanggal)
        rgCategory = findViewById(R.id.rgCategory)
        etFeedback = findViewById(R.id.etFeedback)
        btnSubmit = findViewById(R.id.btnSubmit)
        // Pastikan Anda menambahkan ProgressBar dengan ID ini di XML Anda
        // progressBar = findViewById(R.id.progressBar)

        val ivBack: ImageView = findViewById(R.id.ivBack)
        ivBack.setOnClickListener {
            finish()
        }

        // Setup Date Picker
        etTanggal = findViewById(R.id.etTanggal)
        etTanggal.setOnClickListener {
            showDatePickerDialog()
        }

        // Setup Tombol Submit
        btnSubmit.setOnClickListener {
            // Panggil fungsi untuk memproses dan menyimpan feedback
            submitFeedback()
        }
    }
    private fun submitFeedback() {
        val date = etTanggal.text.toString().trim()
        val feedbackContent = etFeedback.text.toString().trim()
        val selectedCategoryId = rgCategory.checkedRadioButtonId
        if (selectedCategoryId == -1) {
            Toast.makeText(this, "Silakan pilih kategori", Toast.LENGTH_SHORT).show()
            return
        }
        val selectedRadioButton = findViewById<RadioButton>(selectedCategoryId)
        val category = selectedRadioButton.text.toString()

        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(feedbackContent)) {
            Toast.makeText(this, "Tanggal dan isi feedback tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Gagal mendapatkan data user. Silakan login ulang.", Toast.LENGTH_SHORT).show()
            return
        }

        btnSubmit.isEnabled = false
        // progressBar.visibility = View.VISIBLE

        val feedbackData = hashMapOf(
            "userId" to userId,
            "date" to date,
            "category" to category,
            "feedbackContent" to feedbackContent,
            "timestamp" to FieldValue.serverTimestamp()
        )

        firestore.collection("feedback")
            .add(feedbackData)
            .addOnSuccessListener {
                Toast.makeText(this, "Feedback berhasil terkirim. Terima kasih!", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener { e ->
                btnSubmit.isEnabled = true
                // progressBar.visibility = View.GONE
                Toast.makeText(this, "Gagal mengirim feedback: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                // Format tanggal menjadi "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                etTanggal.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}