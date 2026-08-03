package com.example.myview

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myview.databinding.ActivityMainBinding
import com.example.myview.fragment.FragmentHome
import androidx.fragment.app.Fragment
import com.example.myview.fragment.FragmentCart
import com.example.myview.fragment.FragmentFavourite
import com.example.myview.fragment.FragmentMore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainFrame)
        { v, insets -> val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets }

        binding.bottomNav.navforshop.setOnClickListener {
            loadFragment(FragmentHome())
            updateBottomNav("shop")

        }

        binding.bottomNav.navtroll.setOnClickListener {
            loadFragment(FragmentCart())
            updateBottomNav("cart")
        }

        binding.bottomNav.navFavorite.setOnClickListener {
             loadFragment(FragmentFavourite())
            updateBottomNav("favorite")
        }

        binding.bottomNav.navDots.setOnClickListener {
             loadFragment(FragmentMore())
            updateBottomNav("more")
        }

        if (savedInstanceState == null) {

            if (intent.getStringExtra("open_fragment") == "cart") {

                loadFragment(FragmentCart())
                updateBottomNav("cart")

            } else {

                loadFragment(FragmentHome())
                updateBottomNav("shop")

            }
        }
    }
    private fun updateBottomNav(selected: String) {

        // Hide all texts
        binding.bottomNav.shoptext.visibility = View.GONE
        binding.bottomNav.cartText.visibility = View.GONE
        binding.bottomNav.favouritetext.visibility = View.GONE
        binding.bottomNav.moretext.visibility = View.GONE

        // Remove all backgrounds
        binding.bottomNav.navforshop.setBackgroundResource(android.R.color.transparent)
        binding.bottomNav.navforcart.setBackgroundResource(android.R.color.transparent)
        binding.bottomNav.navforfavourite.setBackgroundResource(android.R.color.transparent)
        binding.bottomNav.navformore.setBackgroundResource(android.R.color.transparent)

        when (selected) {

            "shop" -> {
                binding.bottomNav.shoptext.visibility = View.VISIBLE
                binding.bottomNav.navforshop.setBackgroundResource(R.drawable.bg_bottomnav)
            }

            "cart" -> {
                binding.bottomNav.cartText.visibility = View.VISIBLE
                binding.bottomNav.navforcart.setBackgroundResource(R.drawable.bg_bottomnav)
            }

            "favorite" -> {
                binding.bottomNav.favouritetext.visibility = View.VISIBLE
                binding.bottomNav.navforfavourite.setBackgroundResource(R.drawable.bg_bottomnav)
            }

            "more" -> {
                binding.bottomNav.moretext.visibility = View.VISIBLE
                binding.bottomNav.navformore.setBackgroundResource(R.drawable.bg_bottomnav)

            }

        }


    }
        private fun loadFragment(fragment: Fragment){

            supportFragmentManager.beginTransaction()
                .replace(binding.mainFrame.id, fragment)
                .commit()


        }


}