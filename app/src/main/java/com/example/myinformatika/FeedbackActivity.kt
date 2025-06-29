package com.example.myinformatika

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FeedbackActivity : AppCompatActivity() {

    private lateinit var etTanggal: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

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
        val btnSubmit: MaterialButton = findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            // Logika untuk mengirim data (untuk sekarang kita tampilkan Toast)
            Toast.makeText(this, "Feedback terkirim!", Toast.LENGTH_SHORT).show()
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