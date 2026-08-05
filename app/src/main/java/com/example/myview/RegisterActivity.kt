package com.example.myview

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//import com.example.myview.databinding.ActivityMainBinding
import com.example.myview.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding //declare binding variable (binding doesnt contain anything rn

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) { // fundamentals of android activity (AppCompactActivity has its own onCreate but we want our own version of onCreate and android automatically calls when your activity is created
        super.onCreate(savedInstanceState) //let the parent do its setup(AppCompactActivity)

        binding = ActivityRegisterBinding.inflate(layoutInflater) //initializes the binding (inflates(creates) the activity_register.xml layout and creates an ActivityRegisterBinding object and stores itg in the binding variable)
        setContentView(binding.root) // displays the layout

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance() //initialize and asking for the object of the firebase authentication(give me authentication service for my app) then returns the firebaseauth object and stores it in auth( the services part is hidden that is done by google services)

        binding.btnRegister.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) { // the user doesnt know which field is missing
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }

            }
        binding.txtLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}
