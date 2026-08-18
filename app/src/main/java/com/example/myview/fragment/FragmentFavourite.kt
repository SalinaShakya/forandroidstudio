package com.example.myview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.myview.data.CartManager
import com.example.myview.fragment.FavoritesScreen



class FragmentFavourite : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val count = CartManager.cartItems.sumOf { it.quantity }
                FavoritesScreen(
                    itemCount = count,
                    onBackClick = {
                        // This is valid here because it's inside a Fragment
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                        //onbackpressed android looks at that pile of history and pops the last one instead of closing the app
                        // acts like a pop command? removes the current screen from the top and brings the previous one back
                    }
                )
            }

        }
    }
}
