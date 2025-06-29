package com.example.myinformatika

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminAddInfoActivity : AppCompatActivity() {
    private lateinit var tvFileName: TextView  // Changed from imagePreview
    private val PICK_FILE_REQUEST = 2  // Removed unused PICK_IMAGE_REQUEST
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_add_info)

        val titleInput: EditText = findViewById(R.id.editTitle)
        val descInput: EditText = findViewById(R.id.editDesc)
        val btnUploadFile: Button = findViewById(R.id.btnUploadFile)
        val btnSave: Button = findViewById(R.id.btnSave)
        tvFileName = findViewById(R.id.tvFileName)  // Initialize the TextView

        // File Upload Button
        btnUploadFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(intent, PICK_FILE_REQUEST)
        }

        // Status Bar Transparency
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                statusBarColor = Color.TRANSPARENT
                decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        )
            }
        }

        // Save Button
        btnSave.setOnClickListener {
            val title = titleInput.text.toString()
            val desc = descInput.text.toString()

            if (title.isBlank() || desc.isBlank()) {
                Toast.makeText(this, "Judul dan deskripsi wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newInfo = InfoItem(
                category = "Umum",
                title = title,
                description = desc,
                date = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date()),
                views = "0",
                fileName = fileUri?.lastPathSegment ?: ""  // Added file info
            )

            val resultIntent = Intent().apply {
                putExtra("NEW_INFO", newInfo)
                // Add the file URI if needed
                fileUri?.let { putExtra("FILE_URI", it.toString()) }
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                PICK_FILE_REQUEST -> {
                    fileUri = data.data
                    fileUri?.let {
                        val fileName = it.lastPathSegment ?: "unknown_file"
                        tvFileName.text = "File dipilih: $fileName"  // Update UI
                        Toast.makeText(this, "File dipilih: $fileName", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}