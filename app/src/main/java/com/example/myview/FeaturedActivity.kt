package com.example.myview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myview.databinding.ActivityFeaturedBinding
import android.content.Intent
import com.example.myview.adapter.InsideFeaturedAdapter
import com.google.android.material.tabs.TabLayoutMediator
class FeaturedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeaturedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFeaturedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.featuredRoot)
        { v, insets -> val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets }
        val images = listOf(
            R.drawable.featuredshirt,
            R.drawable.wave,
            R.drawable.esewa
        )

        binding.viewPagerImages.adapter = InsideFeaturedAdapter(images)

        TabLayoutMediator(
            binding.tabIndicator,
            binding.viewPagerImages
        ) { _, _ -> }.attach()
        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.imgCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }
}