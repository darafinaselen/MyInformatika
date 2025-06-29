package com.example.myinformatika

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminAcademicInfoActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InfoAdapter
    private lateinit var fabAddInfo: FloatingActionButton
    private val infoList = mutableListOf<InfoItem>()

    companion object {
        private const val ADD_INFO_REQUEST = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_academic_info)

        // Transparansi status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                statusBarColor = Color.TRANSPARENT
                decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        )
            }
        }

        // Inisialisasi komponen
        recyclerView = findViewById(R.id.recyclerAcademicInfo)
        fabAddInfo = findViewById(R.id.fab_add_info)

        // Inisialisasi adapter dengan aksi hapus
        adapter = InfoAdapter(infoList) { itemToDelete ->
            val index = infoList.indexOf(itemToDelete)
            if (index != -1) {
                infoList.removeAt(index)
                adapter.notifyItemRemoved(index)
                Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Event klik tombol tambah info
        fabAddInfo.setOnClickListener {
            val intent = Intent(this, AdminAddInfoActivity::class.java)
            startActivityForResult(intent, ADD_INFO_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == ADD_INFO_REQUEST) {
            data?.getParcelableExtra<InfoItem>("NEW_INFO")?.let { newInfo ->
                infoList.add(0, newInfo)
                adapter.notifyItemInserted(0)
                recyclerView.scrollToPosition(0)
            }
        }
    }
}
