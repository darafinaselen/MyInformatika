package com.example.myinformatika

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val titleTextView = findViewById<TextView>(R.id.articleactivityTitle)
        val dateTextView = findViewById<TextView>(R.id.articleactivityDate)
        val contentTextView = findViewById<TextView>(R.id.articleactivityContent)
        val articleImageView = findViewById<ImageView>(R.id.articleactivityImage)


        // Data dari Intent
        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val content = intent.getStringExtra("content")
        val imageUrl = intent.getStringExtra("imageUrl")

        titleTextView.text = title
        dateTextView.text = date
        contentTextView.text = content

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.article_image)
            .into(articleImageView)
    }
}