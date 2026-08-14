package com.example.myview.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myview.LoginActivity
import com.example.myview.R
import com.example.myview.RegisterActivity
import com.example.myview.adapter.CartAdapter
import com.example.myview.data.CartManager
import com.example.myview.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth

class FragmentCart : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            // USER MODE: Show cart
            binding.layoutCartUser.visibility = View.VISIBLE
            binding.layoutCartGuest.visibility = View.GONE
            setupCartList()
        } else {
            // GUEST MODE: Show login prompt
            binding.layoutCartUser.visibility = View.GONE
            binding.layoutCartGuest.visibility = View.VISIBLE
            setupGuestView()
        }
    }

    private fun setupCartList() {
        val adapter = CartAdapter(CartManager.cartItems) {
            updateTotalPrice()
        }
        binding.rvCart.adapter = adapter

        binding.btnCheckout.setOnClickListener {
            // Logic for checkout can go here (e.g., navigate to CheckoutActivity)
            // val intent = Intent(requireContext(), CheckoutActivity::class.java)
            // startActivity(intent)
        }

        // Initialize and load items
        CartManager.init(requireContext()) {
            adapter.notifyDataSetChanged()
            updateTotalPrice()
        }

        updateTotalPrice()
    }

    private fun setupGuestView() {
        // Handle Login button inside the included layout
        binding.loginOrSignLayout.btnLoginInclude.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        // Handle Sign Up button inside the included layout
        binding.loginOrSignLayout.btnSignUpInclude.setOnClickListener {
            startActivity(Intent(requireContext(), RegisterActivity::class.java))
        }
    }

    private fun updateTotalPrice() {
        val total = CartManager.cartItems.sumOf { it.price * it.quantity }
        binding.txtTotal.text = getString(R.string.rs, total.toString())
        
        // Update item count label
        val count = CartManager.cartItems.sumOf { it.quantity }
        binding.item.text = "Items ($count)"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
