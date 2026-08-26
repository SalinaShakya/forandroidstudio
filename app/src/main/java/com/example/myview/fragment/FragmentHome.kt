package com.example.myview.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
        setupObservers()

        viewModel.fetchHomeData()

        binding.notification.setOnClickListener {
            startActivity(Intent(requireContext(), NotificationActivity::class.java))
        }
    }

    private fun setupObservers() {

        viewModel.featuredProducts.observe(viewLifecycleOwner) { list ->
            binding.featuredRecyclerView.adapter = FeaturedProductsAdapter(list)
        }

        viewModel.popularBrands.observe(viewLifecycleOwner) { list ->
            binding.popularrecycler.adapter = PopularBrandAdapter(list)
        }

        viewModel.recommendedItems.observe(viewLifecycleOwner) { list ->
            binding.recommendedrecycler.adapter = FeaturedProductsAdapter(list)
        }

        viewModel.hotDeals.observe(viewLifecycleOwner) { deals ->
            binding.hotdealscarousel.adapter = FeaturedProductsAdapter(deals)
        }

        viewModel.popularCategories.observe(viewLifecycleOwner) { categories ->
            val flexboxLayoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.FLEX_START
            }

            binding.mostPopularTagsRecycler.layoutManager = flexboxLayoutManager
            binding.mostPopularTagsRecycler.adapter = PopularAdapter(categories.take(7))
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            viewLifecycleOwner.lifecycleScope.launch {
                delay(3000L.milliseconds)
                binding.loadingLayout.visibility =
                    if (loading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupRecyclerViews() {

        binding.carouselRecyclerView.setHasFixedSize(true)
//        binding.carouselRecyclerView.layoutManager = CarouselLayoutManager()
        binding.carouselRecyclerView.layoutManager = CarouselLayoutManager(
            com.google.android.material.carousel.MultiBrowseCarouselStrategy()
        )
        val snapHelper = CarouselSnapHelper()
        snapHelper.attachToRecyclerView(binding.carouselRecyclerView)
        binding.carouselRecyclerView.isNestedScrollingEnabled = false

        val imageList = listOf(
            R.drawable.yellowbanner,
            R.drawable.esewa,
            R.drawable.greenbanner
        )

        binding.carouselRecyclerView.adapter = CarouselAdapter(imageList.toMutableList())
        setupCarouselIndicator(imageList, snapHelper)

        binding.categoriesRecyclerView.setHasFixedSize(true)
        binding.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        binding.featuredRecyclerView.itemAnimator = null
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

    private fun setupCarouselIndicator(imageList: List<Int>,snapHelper: CarouselSnapHelper) {
        //list-onscrolllistner(scrollidlestate)-snaphelper-map(positiondetection)-dots(statetrigger)-dot.isselected=true
        val context = requireContext()
        val indicatorContainer = binding.carouselIndicatorContainer
        indicatorContainer.removeAllViews()

        val dots = mutableListOf<ImageView>()
        for (i in imageList.indices) {
            val dot = ImageView(context).apply {
                setImageResource(R.drawable.dots)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 0, 8, 0) // Added horizontal margins
                }
                isSelected = (i == 0)
            }
            dots.add(dot)
            indicatorContainer.addView(dot)
        }

        // Use a variable for the SnapHelper to find the snapped view accurately
//        val snapHelper = CarouselSnapHelper()
        // If you already attached one in setupRecyclerViews,  reuse
        // or just attach this new one here.

        binding.carouselRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager
                    val centerView = snapHelper.findSnapView(layoutManager)

                    if (centerView != null) {
                        val position = layoutManager?.getPosition(centerView) ?: 0
                        dots.forEachIndexed { index, dot ->
                            dot.isSelected = (index == position)
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshQuantitiesOnly()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}