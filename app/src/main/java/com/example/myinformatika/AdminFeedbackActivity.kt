package com.example.myinformatika

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.button.MaterialButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class AdminFeedbackActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var feedbackRecycler: RecyclerView
    private var adapter: FeedbackAdapter? = null

    private lateinit var autoCompleteCategory: AutoCompleteTextView
    private lateinit var autoCompleteAcademicYear: AutoCompleteTextView
    private lateinit var btnApplyFilter: MaterialButton
    private lateinit var btnResetFilter: MaterialButton
    private lateinit var ivBack: ImageView
    private lateinit var tvEmptyData: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_feedback)

        db = FirebaseFirestore.getInstance()

        ivBack = findViewById(R.id.ivBack)
        feedbackRecycler = findViewById(R.id.feedbackRecycler)
        autoCompleteCategory = findViewById(R.id.autoCompleteCategory)
        autoCompleteAcademicYear = findViewById(R.id.autoCompleteAcademicYear)
        btnApplyFilter = findViewById(R.id.btnApplyFilter)
        btnResetFilter = findViewById(R.id.btnResetFilter)
        tvEmptyData = findViewById(R.id.tvEmptyData)

        ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        setupCategoryDropdown()
        setupAcademicYearDropdown()
        setupOrUpdateAdapter(createBaseQuery())

        btnApplyFilter.setOnClickListener { applyFilters() }
        btnResetFilter.setOnClickListener { resetFilters() }
    }

    private fun setupCategoryDropdown() {
        val categories = listOf("Academic", "Course")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        autoCompleteCategory.setAdapter(categoryAdapter)
    }

    private fun setupAcademicYearDropdown() {
        val academicYears = mutableListOf<String>()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        for (i in 0..5) {
            val year = currentYear - i
            academicYears.add("$year Genap")
            academicYears.add("$year Ganjil")
        }
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, academicYears)
        autoCompleteAcademicYear.setAdapter(yearAdapter)
    }

    private fun setupOrUpdateAdapter(query: Query) {
        adapter?.stopListening()

        val options = FirestoreRecyclerOptions.Builder<FeedbackItem>()
            .setQuery(query, FeedbackItem::class.java)
            .build()

        adapter = object : FeedbackAdapter(options, this) {
            override fun onDataChanged() {
                super.onDataChanged()
                val isEmpty = itemCount == 0
                tvEmptyData.visibility = if (isEmpty) View.VISIBLE else View.GONE
                feedbackRecycler.visibility = if (isEmpty) View.GONE else View.VISIBLE
            }

            override fun onError(e: com.google.firebase.firestore.FirebaseFirestoreException) {
                super.onError(e)
                Log.e("FirestoreAdapterError", "Error: ${e.message}", e)
            }
        }

        feedbackRecycler.itemAnimator = null
        feedbackRecycler.layoutManager = LinearLayoutManager(this)
        feedbackRecycler.adapter = adapter

        adapter?.startListening()
    }

    private fun applyFilters() {
        // memanggil metode utama dengan query yang sudah difilter
        val filteredQuery = createFilteredQuery()
        setupOrUpdateAdapter(filteredQuery)
    }

    private fun resetFilters() {
        autoCompleteCategory.setText("", false)
        autoCompleteAcademicYear.setText("", false)
        currentFocus?.clearFocus()
        val baseQuery = createBaseQuery()
        setupOrUpdateAdapter(baseQuery)
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    private fun createBaseQuery(): Query {
        return db.collection("feedback").orderBy("timestamp", Query.Direction.DESCENDING)
    }

    private fun createFilteredQuery(): Query {
        var query: Query = db.collection("feedback")

        val category = autoCompleteCategory.text.toString()
        val academicYear = autoCompleteAcademicYear.text.toString()

        if (category.isNotEmpty()) {
            query = query.whereEqualTo("category", category)
        }

        if (academicYear.isNotEmpty()) {
            try {
                val (startDate, endDate) = parseAcademicYear(academicYear)
                if (startDate != null && endDate != null) {
                    query = query.whereGreaterThanOrEqualTo("timestamp", Timestamp(startDate))
                        .whereLessThanOrEqualTo("timestamp", Timestamp(endDate))
                }
            } catch (e: Exception) {
                Log.e("ADMIN_FEEDBACK_ERROR", "Gagal parsing tahun akademik: $academicYear", e)
            }
        }

        return query.orderBy("timestamp", Query.Direction.DESCENDING)
    }

    private fun parseAcademicYear(academicYear: String): Pair<Date?, Date?> {
        val parts = academicYear.split(" ")
        if (parts.size != 2) return Pair(null, null)
        val year = parts[0].toInt()
        val semester = parts[1]
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        return when (semester.lowercase(Locale.ROOT)) {
            "genap" -> {
                calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0)
                val start = calendar.time
                calendar.set(year, Calendar.JUNE, 30, 23, 59, 59)
                val end = calendar.time
                Pair(start, end)
            }
            "ganjil" -> {
                calendar.set(year, Calendar.JULY, 1, 0, 0, 0)
                val start = calendar.time
                calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59)
                val end = calendar.time
                Pair(start, end)
            }
            else -> Pair(null, null)
        }
    }
}