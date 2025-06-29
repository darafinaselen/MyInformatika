package com.example.myinformatika
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_article)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_article
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.navigation_article -> {
                    true
                }
                R.id.navigation_info -> {
//                    val intent = Intent(this, LabInfoActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//                    startActivity(intent)
//                    finish()
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        val fillArticleCard = findViewById<CardView>(R.id.FillArticle)
        fillArticleCard.setOnClickListener {
            val intent = Intent(this, ArticleActivity::class.java)
            startActivity(intent)
        }


    }


}

