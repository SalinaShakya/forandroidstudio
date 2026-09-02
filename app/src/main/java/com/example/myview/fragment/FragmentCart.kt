package com.example.myview.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.example.myview.LoginActivity
import com.example.myview.R
import com.example.myview.RegisterActivity
import com.example.myview.adapter.CartAdapter
import com.example.myview.data.CartManager
import com.example.myview.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth

import androidx.compose.material3.Text
import androidx.compose.ui.semantics.text
import androidx.fragment.app.viewModels
import com.example.myview.CheckoutActivity
import com.example.myview.ui.viewmodel.CartViewModel

//now since i made a ViewModel for the math
//the fragment now manages the ui as in whenever the totalPrice and others do change the
//ViewModel tells the Fragment to update its ui the textView ig

//okay so what changed is that i dont have to be calling the updateTotalPrice multiple times even if nothing changes
//the main enhancement for using the ViewModel is that when i rotate my screen i dont have to do the math all over again
//efficient, rotation, and all the math changes in one place
//the fragment only points out
class FragmentCart : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!


    // 1stly to use the ViewModel i link
    private val viewModel: CartViewModel by viewModels()

    //so this is needed
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
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        binding.myComposeView.setContent {
        // Call my Composable function
        TestPreview()}
    }

    //till here its the FragmentCart
    //so the setupCartList changes
//    private fun setupCartList() {
//        val adapter = CartAdapter(CartManager.cartItems) {
//            updateTotalPrice()
//        }
//        binding.rvCart.adapter = adapter
//
//        binding.btnCheckout.setOnClickListener {
//            // Logic for checkout can go here (e.g., navigate to CheckoutActivity)
//            // val intent = Intent(requireContext(), CheckoutActivity::class.java)
//            // startActivity(intent)
//        }
//
//        // Initialize and load items
//        CartManager.init(requireContext()) {
//            adapter.notifyDataSetChanged()
//            updateTotalPrice()
//        }
//        updateTotalPrice()
//
//        binding.btnCheckout.setOnClickListener {
//            // 1. Create the intent for CheckoutActivity
//            val intent = Intent(requireContext(), CheckoutActivity::class.java)
//
//            // 2. Start the activity
//            startActivity(intent)
//        }
//    }
    private fun setupCartList() {
        // 2. Initialize Adapter with the shared manager list
        val adapter = CartAdapter(CartManager.cartItems,
            onPlusClick = { item -> viewModel.incrementQuantity(item) },
            onMinusClick = { item -> viewModel.decrementQuantity(item) }
        )
        binding.rvCart.adapter = adapter

        // 3. OBSERVE: Automatically update UI when data changes

        // Listen for list changes
        viewModel.cartItems.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        // Listen for Price changes
        viewModel.totalPrice.observe(viewLifecycleOwner) { total ->
            binding.txtTotal.text = "Rs. $total"
        }

        // Listen for Item Count and Badge changes
        viewModel.itemCount.observe(viewLifecycleOwner) { count ->
            binding.item.text = "Items ($count)"
            if (count > 0) {
                binding.txtCartBadge.visibility = View.VISIBLE
                binding.txtCartBadge.text = count.toString()
            } else {
                binding.txtCartBadge.visibility = View.GONE
            }
        }

        // Checkout Navigation
        binding.btnCheckout.setOnClickListener {
            startActivity(Intent(requireContext(), CheckoutActivity::class.java))
        }

        // Ensure Manager is initialized (loads from Room)
        CartManager.init(requireContext())
    }

    //the setupGuestView is the same ig
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

    //the updateTotalPrice is removed
//    private fun updateTotalPrice() {
//        val total = CartManager.cartItems.sumOf { it.price * it.quantity }
//        binding.txtTotal.text = getString(R.string.rs, total.toString())
//
//        // Update item count label
//        val count = CartManager.cartItems.sumOf { it.quantity }
//        binding.item.text = "Items ($count)"
//        updateTotal() // <--- Add this call
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //idk why is this twice
//    private fun updateTotal() {
//        val total = CartManager.cartItems.sumOf { it.price * it.quantity }
//        binding.txtTotal.text = getString(R.string.rs, total.toString())
//
//        // 1. Calculate total items
//        val count = CartManager.cartItems.sumOf { it.quantity }
//        binding.item.text = "Items ($count)"
//
//        // 2. Add THIS BLOCK to update the Badge!
//        if (count > 0) {
//            binding.txtCartBadge.visibility = View.VISIBLE
//            binding.txtCartBadge.text = count.toString()
//        } else {
//            binding.txtCartBadge.visibility = View.GONE
//        }
//    }

    @Preview(showBackground = true)
    @Composable
    fun TestPreview() {
        Text(text = "Compose is Working, Salinanana!")
    }
}
