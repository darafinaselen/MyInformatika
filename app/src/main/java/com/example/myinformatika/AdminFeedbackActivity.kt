package com.example.myinformatika

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminFeedbackActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterRecycleView
    private val feedbackList = mutableListOf<FeedbackItem>()

    private val db = FirebaseFirestore.getInstance()
    private val TAG = "MainActivityDebug"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Activity started") // Log 1
        setContentView(R.layout.activity_admin_feedback)

        val ivBack: ImageView = findViewById(R.id.ivBack)
        ivBack.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.feedbackRecycler)

        adapter = AdapterRecycleView(feedbackList,
            onDetailClick = { item -> Toast.makeText(this, "Detail feedback dari ${item.sender}: ${item.feedbackSnippet}", Toast.LENGTH_SHORT).show() },
            onDeleteClick = { item -> Toast.makeText(this, "Hapus feedback dari ${item.sender}", Toast.LENGTH_SHORT).show() },
            onCheckboxChecked = { item, isChecked ->
                Toast.makeText(this, "Checkbox untuk ${item.sender} ${if (isChecked) "tercentang" else "tidak tercentang"}", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        Log.d(TAG, "onCreate: Calling fetchFeedbackFromFirestore()") // Log 2
        fetchFeedbackFromFirestore()
    }

    private fun fetchFeedbackFromFirestore() {
        Log.d(TAG, "fetchFeedbackFromFirestore: Attempting to fetch data from Firestore") // Log 3
        db.collection("feedback")
            .get()
            .addOnSuccessListener { result ->
                Log.d(TAG, "fetchFeedbackFromFirestore: Data fetch successful!") // Log 4
                feedbackList.clear()
                for (document in result) {
                    Log.d("FirestoreData", "${document.id} => ${document.data}") // Log 5 (sudah ada)
                    val sender = document.getString("sender") ?: "N/A"
                    val year = document.getString("year") ?: "N/A"
                    val category = document.getString("category") ?: "N/A"
                    val feedbackSnippet = document.getString("feedbackSnippet") ?: "Tidak ada cuplikan"
                    val showDelete = document.getBoolean("showDelete") ?: true

                    val feedbackItem = FeedbackItem(sender, year, category, feedbackSnippet, showDelete)
                    feedbackList.add(feedbackItem)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "fetchFeedbackFromFirestore: Error getting documents: ", exception) // Log 6 (ubah ke Log.e untuk lebih jelas)
                Toast.makeText(this, "Gagal memuat data: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }
}