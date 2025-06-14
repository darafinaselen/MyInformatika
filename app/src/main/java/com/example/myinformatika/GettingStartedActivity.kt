package com.example.myinformatika

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.view.View

class GettingStartedActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ImageSliderAdapter
    private val sliderHandler = Handler(Looper.getMainLooper())
    private val imageList = listOf(
        R.drawable.img_getting_started,
        R.drawable.img_getting_started2,
        R.drawable.img_getting_started3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getting_started)

        viewPager = findViewById(R.id.viewPagerImages)
        val tvAppName: TextView = findViewById(R.id.tvAppName)

        val indicators = listOf<View>(
            findViewById(R.id.indicator1),
            findViewById(R.id.indicator2),
            findViewById(R.id.indicator3)
        )

        adapter = ImageSliderAdapter(imageList)
        viewPager.adapter = adapter

        val colors = listOf(
            ContextCompat.getColor(this, R.color.primaryblue600),
            ContextCompat.getColor(this, R.color.primaryblue700),
            ContextCompat.getColor(this, R.color.primaryblue800)
        )

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicators.forEachIndexed { index, indicatorView ->
                    val backgroundResId = if (index == position) {
                        R.drawable.indicator_bar_active
                    } else {
                        R.drawable.indicator_bar_default
                    }
                    indicatorView.setBackgroundResource(backgroundResId)
                }
                tvAppName.setTextColor(colors[position])
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })

        val getStartedButton: Button = findViewById(R.id.btnGetStarted)
        getStartedButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private val sliderRunnable = Runnable {
        viewPager.currentItem = (viewPager.currentItem + 1) % adapter.itemCount
    }
    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }
}