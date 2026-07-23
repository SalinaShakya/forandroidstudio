package com.example.myview

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

//        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainFrame, FragmentHome())
                .commit()
        }
    }
    private fun updateBottomNav(selected: String) {

        binding.bottomNav.shoptext.visibility =
            if (selected == "shop") View.VISIBLE else View.GONE

        binding.bottomNav.cartText.visibility =
            if (selected == "cart") View.VISIBLE else View.GONE

        binding.bottomNav.favouritetext.visibility =
            if (selected == "favorite") View.VISIBLE else View.GONE

        binding.bottomNav.moretext.visibility =
            if (selected == "more") View.VISIBLE else View.GONE
    }
        private fun loadFragment(fragment: Fragment){

            supportFragmentManager.beginTransaction()
                .replace(binding.mainFrame.id, fragment)
                .commit()

        }


}