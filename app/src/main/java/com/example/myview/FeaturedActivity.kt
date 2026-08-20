package com.example.myview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myview.databinding.ActivityFeaturedBinding
import android.content.Intent
import android.graphics.Color
import com.example.myview.adapter.InsideFeaturedAdapter
import com.google.android.material.tabs.TabLayoutMediator
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.myview.data.api.RetrofitClient
import android.util.Log
import android.widget.Toast
import com.example.myview.data.FavoriteManager
import com.example.myview.data.local.FavoriteEntity

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

        val productId = intent.getIntExtra("product_id", -1)
        if (productId != -1) {
            fetchProductDetails(productId)
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.imgCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

    }

    private fun fetchProductDetails(productId: Int) {
        lifecycleScope.launch {
            try {
                val product = RetrofitClient.apiService.getProductDetails(productId)
                
                binding.txtName.text = product.title
                binding.txtPrice.text = "Rs. ${product.price}"
                
                // For the ViewPager, we wrap the single image in a list
                val images = listOf(product.image)
                binding.viewPagerImages.adapter = InsideFeaturedAdapter(images)
                
                TabLayoutMediator(
                    binding.tabIndicator,
                    binding.viewPagerImages
                ) { _, _ -> }.attach()

                binding.txtDescrip.text = product.description

                binding.likeButton.setOnClickListener {
                    // product is the data you fetched from the API
                    val favorite = FavoriteEntity(
                        id = product.id,
                        title = product.title,
                        price = product.price,
                        image = product.image
                    )
                    FavoriteManager.addFavorite(favorite)
//                    Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()

                }
                val isCurrentlyFavorite = FavoriteManager.favorites.any { it.id == product.id }

                if (isCurrentlyFavorite) {
                    binding.imgLike.setColorFilter(Color.parseColor("#43C230")) }
                else {
                    binding.imgLike.clearColorFilter() }
            } catch (e: Exception) {
                Log.e("FeaturedActivity", "Error fetching product: ${e.message}")
                Toast.makeText(this@FeaturedActivity, "Error loading product details", Toast.LENGTH_SHORT).show()
            }
        }
    }
}