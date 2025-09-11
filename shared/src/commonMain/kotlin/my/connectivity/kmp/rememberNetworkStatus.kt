package my.connectivity.kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
expect fun rememberNetworkStatus(): State<Boolean>