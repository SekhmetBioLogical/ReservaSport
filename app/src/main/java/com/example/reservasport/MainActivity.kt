package com.example.reservasport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.reservasport.ui.screens.ReservaSportScreen
import com.example.reservasport.ui.theme.ReservaSportTheme // <--- Importamos tu tema real

class MainActivity : ComponentActivity() {
    // ✅ Sin companion object - eliminada referencia estática que causaba memory leak

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReservaSportTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ReservaSportScreen()
                }
            }
        }
    }
}