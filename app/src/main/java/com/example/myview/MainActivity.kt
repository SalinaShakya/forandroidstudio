package com.example.myview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myview.databinding.ActivityMainBinding
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.CarouselLayoutManager
class MainActivity : AppCompatActivity() {

    // 1. Move variable declarations to the top of the class, OUTSIDE of onCreate
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 2. Initialize View Binding BEFORE using it or setting the layout
        binding = ActivityMainBinding.inflate(layoutInflater)

        // 3. Enable edge-to-edge layout styling
        enableEdgeToEdge()

        // 4. Pass the root layout view directly from the binding object
        setContentView(binding.root)

        // 5. Apply system spacing safely to your root view
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
            Category("Mobile", R.drawable.item),
            Category("Laptop", R.drawable.item__1_),
            Category("Clothing", R.drawable.ic_shop_clothing),
            Category("Grocery", R.drawable.ic_shop_grocery)
        )

        // FIXED: The adapter now cleanly receives the Category list with strings included
        val categoryAdapter = CategoryAdapter(categoryData)
        binding.categoriesRecyclerView.adapter = categoryAdapter

    }
}