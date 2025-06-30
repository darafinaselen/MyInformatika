package com.example.myinformatika

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtiActivity2 : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: ArtikelAdapter
//    private val repository = ArtikelRepository.getInstance()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_arti)
//
//        recyclerView = findViewById(R.id.recyclerViewArtikel)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        // Tampilkan progress bar
//        val progressBar = findViewById<View>(R.id.progressBar)
//        progressBar.visibility = View.VISIBLE
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val artikelList = repository.getAllArtikel()
//
//            withContext(Dispatchers.Main) {
//                progressBar.visibility = View.GONE
//                adapter = ArtikelAdapter(artikelList) { artikel ->
//                    // Handle klik item
//                    // Contoh: Tampilkan detail artikel atau buka Activity baru
//                }
//                recyclerView.adapter = adapter
//            }
//        }
//    }
}
