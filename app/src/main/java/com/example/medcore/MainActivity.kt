package com.example.medcore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.medcore.navigation.MedCoreNavGraph
import com.example.medcore.ui.theme.BackgroundDeep
import com.example.medcore.ui.theme.MedCoreTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedCoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color    = BackgroundDeep
                ) {
                    val navController = rememberNavController()
                    MedCoreNavGraph(navController = navController)
                }
            }
        }
    }
}