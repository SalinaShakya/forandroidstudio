package com.example.myview.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myview.LoginActivity
import com.example.myview.RegisterActivity
import com.example.myview.databinding.FragmentMoreBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.myview.databinding.LogoutBinding

class FragmentMore : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            // 1. The Log Out button listener
                binding.layoutLogout.LogOutButtonInclude.setOnClickListener {                // A. Sign out from Firebase
                FirebaseAuth.getInstance().signOut()

                // B. Clear the Firestore details manually from the screen (Optional but good)
                binding.txtetName.text = "User"
                binding.txtetNumber.text = ""

                // C. Flip the UI back to Guest mode immediately
                binding.layoutLoggedIn.visibility = View.GONE
                binding.layoutGuest.visibility = View.VISIBLE

                // D. Show a confirmation message
                Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            }
            // 1. Show the "Logged In" layout
            binding.layoutLoggedIn.visibility = View.VISIBLE
            binding.layoutGuest.visibility = View.GONE

            // 2. Fetch extra details from Firestore
            FirebaseFirestore.getInstance().collection("Users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (isAdded && document != null && document.exists()) {
                        binding.txtetName.text = document.getString("name") ?: "User"
                        binding.txtetNumber.text = document.getString("phone") ?: "No phone"
                    }
                }
        } else {
            // 3. Show "Guest" layout
            binding.layoutLoggedIn.visibility = View.GONE
            binding.layoutGuest.visibility = View.VISIBLE

            // Handle Guest Login button
//            binding.btnLoginGuest.setOnClickListener {
//                startActivity(Intent(requireContext(), LoginActivity::class.java))
            // 1. The Login Button inside the include
            binding.guestLoginLayout.btnLoginInclude.setOnClickListener {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }

// 2. The Sign Up Button inside the include
            binding.guestLoginLayout.btnSignUpInclude.setOnClickListener {
                startActivity(Intent(requireContext(), RegisterActivity::class.java))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
