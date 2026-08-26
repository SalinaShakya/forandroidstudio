package com.example.myview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.myview.data.FavoriteManager.favorites
import com.example.myview.fragment.FavoriteEmptyScreen
import com.example.myview.fragment.FavoritesScreen
import com.example.myview.fragment.FragmentHome

class FragmentFavourite : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                if (favorites.isEmpty()) {
                    FavoriteEmptyScreen(
                        onBackClick = {
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        },
                        onCartClick = {
                            (requireActivity() as MainActivity).openCartTab()
                        },
                        onContinueShopping = {
                            (requireActivity() as MainActivity)
                                .onBackPressedDispatcher
                                .onBackPressed()
                        }
                    )
                } else {
                    FavoritesScreen(
                        items = favorites,
                        onBackClick = {
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        },
                        onCartClick = {
                            (requireActivity() as MainActivity).openCartTab()
                        }
                    )
                }
            }
        }
    }
}