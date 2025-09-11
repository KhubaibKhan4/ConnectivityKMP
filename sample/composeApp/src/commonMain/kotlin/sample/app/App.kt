package sample.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import my.connectivity.kmp.rememberNetworkStatus

@Composable
fun App() {
    val isNetworkAvailable by rememberNetworkStatus()
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (isNetworkAvailable){
            BasicText("Connected")
        } else {
            BasicText("Disconnected")
        }
    }
}