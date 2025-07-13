package com.example.myinformatika

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class FormInfoAdminActivity : AppCompatActivity() {

    private lateinit var inputCategory: EditText
    private lateinit var inputTitle: EditText
    private lateinit var inputDescription: EditText
    private lateinit var inputDate: EditText
    private lateinit var submitButton: Button
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_info_admin)

        firestore = FirebaseFirestore.getInstance()
        inputCategory = findViewById(R.id.inputCategory)
        inputTitle = findViewById(R.id.inputTitle)
        inputDescription = findViewById(R.id.inputDescription)
        inputDate = findViewById(R.id.inputDate)
        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            saveDataToFirebase()
        }
    }

    private fun saveDataToFirebase() {
        val category = inputCategory.text.toString().trim()
        val title = inputTitle.text.toString().trim()
        val description = inputDescription.text.toString().trim()
        val date = inputDate.text.toString().trim()
        val views = "0" // bisa default 0

        if (category.isEmpty() || title.isEmpty() || description.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val collectionRef = firestore.collection("academic_info")
        val documentRef = collectionRef.document()
        val id = documentRef.id

        val data = mapOf(
            "id" to id,
            "category" to category,
            "title" to title,
            "description" to description,
            "date" to date,
            "views" to views
        )

        documentRef.set(data).addOnSuccessListener {
            Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
            clearForm()
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearForm() {
        inputCategory.text.clear()
        inputTitle.text.clear()
        inputDescription.text.clear()
        inputDate.text.clear()
    }
}
