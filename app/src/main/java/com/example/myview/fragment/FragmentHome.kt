package com.example.myview.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myview.R
import com.example.myview.data.api.RetrofitClient
import com.example.myview.adapter.CarouselAdapter
import com.example.myview.adapter.Category
import com.example.myview.adapter.CategoryAdapter
import com.example.myview.adapter.FeaturedProductsAdapter
import com.example.myview.adapter.PopularAdapter
import com.example.myview.adapter.PopularBrandAdapter
import com.example.myview.databinding.FragmentHomeBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import kotlinx.coroutines.launch
import android.content.Intent
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myview.NotificationActivity
import com.example.myview.ui.viewmodel.HomeViewModel
import androidx.fragment.app.viewModels

class FragmentHome : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupObservers() // Start listening for data
//        fetchFeaturedProducts()
//        fetchPopularCategories()
        viewModel.fetchHomeData()
        binding.notification.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setupObservers() {
        // 3. Handle Featured, Popular Brands, and Recommended products
        viewModel.featuredProducts.observe(viewLifecycleOwner) { list ->
            binding.featuredRecyclerView.adapter = FeaturedProductsAdapter(list)

            // Take a slice for Popular Brands
            binding.popularrecycler.adapter = PopularBrandAdapter(list.drop(4).take(4))

            // Take a slice for Recommended
            binding.recommendedrecycler.adapter = FeaturedProductsAdapter(list.drop(8).take(8))
        }
        viewModel.hotDeals.observe(viewLifecycleOwner) { deals ->
            binding.hotdealscarousel.adapter = FeaturedProductsAdapter(deals)
        }

        // 5. Handle Categories
        viewModel.popularCategories.observe(viewLifecycleOwner) { categories ->
            val flexboxLayoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.FLEX_START
            }
            binding.mostPopularTagsRecycler.layoutManager = flexboxLayoutManager
            binding.mostPopularTagsRecycler.adapter = PopularAdapter(categories.take(7))
        }

        // 6. Handle the Loading Spinner
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.loadingLayout.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }
    private fun setupRecyclerViews() {

//        binding.mostPopularTagsRecycler.setHasFixedSize(true)
//        binding.mostPopularTagsRecycler.layoutManager =
//            androidx.recyclerview.widget.StaggeredGridLayoutManager(
//                2,
//                androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
//            )

        binding.carouselRecyclerView.setHasFixedSize(true)
        binding.carouselRecyclerView.layoutManager = CarouselLayoutManager()
        CarouselSnapHelper().attachToRecyclerView(binding.carouselRecyclerView)


        val imageList = mutableListOf(
            R.drawable.yellowbanner,
            R.drawable.esewa
        )

        binding.carouselRecyclerView.adapter = CarouselAdapter(imageList)


        binding.categoriesRecyclerView.setHasFixedSize(true)
        binding.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        val categoryData = listOf(
            Category("Mobile", R.drawable.ic_shop_mobile),
            Category("Electronic Device", R.drawable.ic_shop_computer),
            Category("Fashions", R.drawable.ic_shop_clothing),
            Category("Groceries", R.drawable.ic_shop_grocery)
        )

        binding.categoriesRecyclerView.adapter = CategoryAdapter(categoryData)
    }

//    private fun fetchFeaturedProducts() {
//
//        viewLifecycleOwner.lifecycleScope.launch {
//
//            try {
//
//                val featuredList =
//                    RetrofitClient.apiService.getFeaturedProducts()
//
//                val hotDealsList =
//                    RetrofitClient.apiService.getHotDeals()
//
//                binding.featuredRecyclerView.adapter =
//                    FeaturedProductsAdapter(featuredList)
//
//                binding.hotdealscarousel.adapter =
//                    FeaturedProductsAdapter(hotDealsList)
//
//                val popularBrandProducts =
//                    featuredList.drop(4).take(4)
//
//                binding.popularrecycler.adapter =
//                    PopularBrandAdapter(popularBrandProducts)
//
//                val recommendedProducts =
//                    featuredList.drop(8).take(8)
//
//                binding.recommendedrecycler.adapter =
//                    FeaturedProductsAdapter(recommendedProducts)
//
//            } catch (e: Exception) {
//
//                Log.e("FragmentHome", e.message ?: "Unknown Error")
//
//                Toast.makeText(
//                    requireContext(),
//                    "Could not fetch products",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }

//    private fun fetchPopularCategories() {
//
//        viewLifecycleOwner.lifecycleScope.launch {
//
//            try {
//
//                val apiCategories =
//                    RetrofitClient.apiService.getPopularCategories()
//
//                val limitedCategories = apiCategories.take(7)
//
//                val flexboxLayoutManager =
//                    FlexboxLayoutManager(requireContext()).apply {
//                        flexDirection = FlexDirection.ROW
//                        flexWrap = FlexWrap.WRAP
//                        justifyContent = JustifyContent.FLEX_START
//                    }
//
//                binding.mostPopularTagsRecycler.layoutManager =
//                    flexboxLayoutManager
//
//                binding.mostPopularTagsRecycler.setHasFixedSize(false)
//
//                binding.mostPopularTagsRecycler.adapter =
//                    PopularAdapter(limitedCategories)
//
//            } catch (e: Exception) {
//
//                Log.e(
//                    "FragmentHome",
//                    e.message ?: "Unknown Error"
//                )
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}