package com.example.myview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myview.adapter.InsideFeaturedAdapter
import com.example.myview.data.CartManager
import com.example.myview.data.FavoriteManager
import com.example.myview.data.api.RetrofitClient
import com.example.myview.data.local.FavoriteEntity
import com.example.myview.databinding.ActivityFeaturedBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class FeaturedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeaturedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityFeaturedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateCartBadge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.featuredRoot) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val productId = intent.getIntExtra("product_id", -1)
        if (productId != -1) {
            fetchProductDetails(productId)
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.imgCartFP.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun fetchProductDetails(productId: Int) {
        lifecycleScope.launch {
            try {
                val product = RetrofitClient.apiService.getProductDetails(productId)

                binding.txtName.text = product.title
                binding.txtPrice.text = "Rs. ${product.price}"
                binding.txtDescrip.text = product.description

                val images = listOf(product.image)
                binding.viewPagerImages.adapter = InsideFeaturedAdapter(images)

                TabLayoutMediator(
                    binding.tabIndicator,
                    binding.viewPagerImages
                ) { _, _ -> }.attach()

                // Initial heart state
                val isCurrentlyFavorite = FavoriteManager.favorites.any { it.id == product.id }

                if (isCurrentlyFavorite) {
                    binding.imgLike.setImageResource(R.drawable.ic_temp)
                } else {
                    binding.imgLike.setImageResource(R.drawable.ic_heart)
                }

                // Toggle favorite
                binding.likeButton.setOnClickListener {
                    val favorite = FavoriteEntity(
                        id = product.id,
                        title = product.title,
                        price = product.price,
                        image = product.image
                    )

                    val isFav = FavoriteManager.favorites.any { it.id == product.id }

                    if (isFav) {
                        FavoriteManager.removeFavorite(favorite)
                        binding.imgLike.setImageResource(R.drawable.ic_heart)
                        Toast.makeText(
                            this@FeaturedActivity,
                            "Removed from favorites",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        FavoriteManager.addFavorite(favorite)
                        binding.imgLike.setImageResource(R.drawable.ic_temp)
                        Toast.makeText(
                            this@FeaturedActivity,
                            "Added to favorites",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (e: Exception) {
                Log.e("FeaturedActivity", "Error fetching product: ${e.message}")
                Toast.makeText(
                    this@FeaturedActivity,
                    "Error loading product details",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateCartBadge() {
        val cartCount = CartManager.cartItems.sumOf { it.quantity }

        if (cartCount > 0) {
            binding.txtCartBadgeFP.visibility = View.VISIBLE
            binding.txtCartBadgeFP.text = cartCount.toString()
        } else {
            binding.txtCartBadgeFP.visibility = View.GONE
        }
    }
}