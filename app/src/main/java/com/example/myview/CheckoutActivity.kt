package com.example.myview

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myview.fragment.CheckoutScreen
import com.example.myview.ui.viewmodel.CheckoutViewModel

class CheckoutActivity : AppCompatActivity() {
    private val viewModel: CheckoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CheckoutScreen(
                    viewModel = viewModel,
                    onBackClick = { finish() }
                )
            }
        }
    }
}