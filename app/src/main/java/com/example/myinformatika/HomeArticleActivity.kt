package com.example.myinformatika

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class HomeArticleActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleAdapter
    private val articleList = mutableListOf<ArticleModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_article_activity)

        // Handle padding hanya untuk BottomNavigationView agar tidak terlihat lebih tinggi
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        ViewCompat.setOnApplyWindowInsetsListener(bottomNav) { view, insets ->
            val bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            view.setPadding(0, 0, 0, bottomInset)
            insets
        }

        // Bottom navigation item handler (optional jika pakai menu)
        bottomNav.selectedItemId = R.id.navigation_article
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_article -> {
                    true
                }
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        articleAdapter = ArticleAdapter(articleList)
        recyclerView.adapter = articleAdapter

        // Ambil data dari Firebase
        fetchArticlesFromFirebase()
    }

    private fun fetchArticlesFromFirebase() {
        val database = FirebaseDatabase.getInstance()
        val articlesRef = database.getReference("articles")

        articlesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                articleList.clear()
                for (articleSnapshot in snapshot.children) {
                    val article = articleSnapshot.getValue(ArticleModel::class.java)
                    if (article != null) {
                        articleList.add(article)
                    }
                }
                articleAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Gagal memuat data: ${error.message}")
            }
        })
    }
}
