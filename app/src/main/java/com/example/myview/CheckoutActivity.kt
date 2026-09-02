package com.example.myview

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.example.myview.fragment.CheckoutScreen
import com.example.myview.ui.viewmodel.CheckoutViewModel
import java.io.File

class CheckoutActivity : AppCompatActivity() {
    private val viewModel: CheckoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

enableEdgeToEdge()

        val config = org.osmdroid.config.Configuration.getInstance()
// im a known app so stop blocking me (to save bandwidth)
        config.userAgentValue = "Salina_MyView_V4"
//simply saving the images of the map in my device without needing extra storage permissions
        config.osmdroidBasePath = filesDir
        config.osmdroidTileCache = File(filesDir, "osmdroid/tiles")

//commented load out as im preventing the overwritting of the prevoius block thing
//        org.osmdroid.config.Configuration.getInstance().load(this, android.preference.PreferenceManager.getDefaultSharedPreferences(this))

        //setting my unique id (to not get blocked)
        //as the map may block me if im anonymous to save bandwidth (if i dont provide user agent my apps name)
//        org.osmdroid.config.Configuration.getInstance().userAgentValue = "Salina_MyView_v4"

        //
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