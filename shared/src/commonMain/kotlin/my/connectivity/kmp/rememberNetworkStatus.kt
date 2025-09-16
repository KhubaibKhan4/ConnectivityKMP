package my.connectivity.kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import my.connectivity.kmp.data.model.NetworkStatus

@Composable
expect fun rememberNetworkStatus(): State<NetworkStatus>