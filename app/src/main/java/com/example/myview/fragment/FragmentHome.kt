package com.example.myview.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
//import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myview.NotificationActivity
import com.example.myview.R
import com.example.myview.adapter.CarouselAdapter
import com.example.myview.adapter.Category
import com.example.myview.adapter.CategoryAdapter
import com.example.myview.adapter.FeaturedProductsAdapter
import com.example.myview.adapter.PopularAdapter
import com.example.myview.adapter.PopularBrandAdapter
import com.example.myview.databinding.FragmentHomeBinding
import com.example.myview.ui.viewmodel.HomeViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class FragmentHome : Fragment() {
//    private val viewModel: HomeViewModel by viewModels()
    //viewModels tied to the fragments lifecycle and my mainactivity uses the replace so it when fragment home is destroyed then my viewmodel is also destroyed and when i comeback a new viewmodel is created and my guard thinks its the first time so
    //switched to activityViewModel as now the HomeViewModel lives inside the MainActivity so when i switch the fragment does die but obvi the mainactivity and the viewmodel stay alive and
    //when i return to the home the fragment finds the old ViewModel there
    private val viewModel: HomeViewModel by activityViewModels()

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
        viewModel.fetchHomeData()
        binding.notification.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupObservers() {
        // 1. Featured Products
        viewModel.featuredProducts.observe(viewLifecycleOwner) { list ->
            binding.featuredRecyclerView.adapter = FeaturedProductsAdapter(list)
        }

        // 2. Popular Brands
        viewModel.popularBrands.observe(viewLifecycleOwner) { list ->
            binding.popularrecycler.adapter = PopularBrandAdapter(list)
        }

        // 3. Recommended Items
        viewModel.recommendedItems.observe(viewLifecycleOwner) { list ->
            binding.recommendedrecycler.adapter = FeaturedProductsAdapter(list)
        }

        // 4. Hot Deals
        viewModel.hotDeals.observe(viewLifecycleOwner) { deals ->
            binding.hotdealscarousel.adapter = FeaturedProductsAdapter(deals)
        }

        // 5. Categories
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
            viewLifecycleOwner.lifecycleScope.launch {
                delay(3000L.milliseconds)
                binding.loadingLayout.visibility = if (loading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupRecyclerViews() {
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

        binding.featuredRecyclerView.itemAnimator = null
        binding.hotdealscarousel.itemAnimator = null
        binding.hotdealscarousel.itemAnimator = null

        binding.popularrecycler.itemAnimator = null
        binding.recommendedrecycler.itemAnimator = null




        val categoryData = listOf(
            Category("Mobile", R.drawable.ic_shop_mobile),
            Category("Electronic Device", R.drawable.ic_shop_computer),
            Category("Fashions", R.drawable.ic_shop_clothing),
            Category("Groceries", R.drawable.ic_shop_grocery)
        )
        binding.categoriesRecyclerView.adapter = CategoryAdapter(categoryData)
    }

    override fun onResume() {

        super.onResume()
        // Every time we come back to this screen, refresh the quantities
        viewModel.refreshQuantitiesOnly()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
