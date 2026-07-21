package com.example.myview

import android.os.Bundle

import android.util.Log
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.lifecycle.lifecycleScope

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myview.adapter.FeaturedProductsAdapter
import com.example.myview.databinding.ActivityMainBinding
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.CarouselLayoutManager

import kotlinx.coroutines.launch

import com.example.myview.adapter.PopularAdapter
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response

class MainActivity : AppCompatActivity() {

    // 1. Move variable declarations to the top of the class, OUTSIDE of onCreate
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 2. Initialize View Binding BEFORE using it or setting the layout
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // 3. Enable edge-to-edge layout styling
        enableEdgeToEdge()

        // 4. Pass the root layout view directly from the binding object

        // 5. Apply system spacing safely to your root view
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Add this line inside your MainActivity's onCreate() function
        binding.mostPopularTagsRecycler.setHasFixedSize(true)

// Using a grid approach as an easy native alternative for structural wrapping
        binding.mostPopularTagsRecycler.layoutManager =
            androidx.recyclerview.widget.StaggeredGridLayoutManager(2, androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL)

        // 1. Initialize your Material Carousel component
        binding.carouselRecyclerView.setHasFixedSize(true)
        binding.carouselRecyclerView.layoutManager = CarouselLayoutManager()
        CarouselSnapHelper().attachToRecyclerView(binding.carouselRecyclerView)

        //2.
        binding.categoriesRecyclerView.setHasFixedSize(true)
        binding.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        val imageList = mutableListOf<Int>()
        imageList.add(R.drawable.esewa)
        imageList.add(R.drawable.wave)


        val adapter = CarouselAdapter(imageList)
        binding.carouselRecyclerView.adapter = adapter
//second
        binding.categoriesRecyclerView.setHasFixedSize(true)
        // Using LinearLayoutManager to match your small scrolling icons layout look
        binding.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        val categoryData = listOf(
            Category("Mobile", R.drawable.ic_shop_mobile),
            Category("Electronic Device", R.drawable.ic_shop_computer),
            Category("Fashions", R.drawable.ic_shop_clothing),
            Category("Groceries", R.drawable.ic_shop_grocery)
        )

        // FIXED: The adapter now cleanly receives the Category list with strings included
        val categoryAdapter = CategoryAdapter(categoryData)
        binding.categoriesRecyclerView.adapter = categoryAdapter
        fetchFeaturedProducts()

        fetchPopularCategories()
    }
    private fun fetchFeaturedProducts() {
        lifecycleScope.launch {
            try {
                // Fetch data asynchronously using Retrofit Coroutines
                val featuredList = RetrofitClient.apiService.getFeaturedProducts()
                val hotDealsList = RetrofitClient.apiService.getHotDeals()

                // Reuse the FeaturedProductsAdapter for both areas
                binding.featuredRecyclerView.adapter = FeaturedProductsAdapter(featuredList)
                binding.hotdealscarousel.adapter = FeaturedProductsAdapter(hotDealsList)

            } catch (e: Exception) {
                Log.e("MainActivity", "Error loading product data: ${e.message}")
                Toast.makeText(
                    this@MainActivity,
                    "Could not fetch products",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun fetchPopularCategories() {
        lifecycleScope.launch {
            try {
                // Fetch live list directly from DummyJSON array
                val apiCategories = RetrofitClient.apiService.getPopularCategories()
                val limitedCategories = apiCategories.take(7)
                // Pass the API array directly to your adapter instance on the new element
                val flexboxLayoutManager = com.google.android.flexbox.FlexboxLayoutManager(this@MainActivity).apply {
                    flexDirection = com.google.android.flexbox.FlexDirection.ROW
                    flexWrap = com.google.android.flexbox.FlexWrap.WRAP
                    justifyContent = com.google.android.flexbox.JustifyContent.FLEX_START
                }
                binding.mostPopularTagsRecycler.layoutManager = flexboxLayoutManager
                binding.mostPopularTagsRecycler.setHasFixedSize(false)
                binding.mostPopularTagsRecycler.adapter = PopularAdapter(limitedCategories)

            } catch (e: Exception) {
                Log.e("MainActivity", "Dynamic Tag Fetch Error: ${e.message}")
            }
        }
    }
}