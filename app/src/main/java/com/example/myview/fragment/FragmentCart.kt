package com.example.myview.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myview.R
import com.example.myview.adapter.CartAdapter
import com.example.myview.data.CartManager
import com.example.myview.databinding.FragmentCartBinding

class FragmentCart : Fragment() {
    // Safely initializing View Binding for Fragments
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        return inflater.inflate(R.layout.fragment_cart, container, false)
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }
//    CartManager.cartItems

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setting up the adapter with the callback item update trigger
        val adapter = CartAdapter(CartManager.cartItems) {
            updateTotalPrice()
        }

        binding.rvCart.adapter = adapter
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        // Calculating total using Kotlin's sumOf function
        val total = CartManager.cartItems.sumOf { it.price * it.quantity }
        binding.txtTotal.text = getString(R.string.rs, total.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevents memory leaks
    }
}
