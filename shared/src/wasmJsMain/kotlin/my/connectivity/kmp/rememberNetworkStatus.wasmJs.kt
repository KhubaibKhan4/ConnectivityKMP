package my.connectivity.kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.coroutines.await
import my.connectivity.kmp.data.model.NetworkStatus
import org.w3c.dom.events.Event
import org.w3c.fetch.Response

@Composable
actual fun rememberNetworkStatus(): State<NetworkStatus> {
    return produceState<NetworkStatus>(initialValue = NetworkStatus.Unavailable) {
        // Helper function to check actual internet connectivity
        suspend fun checkInternet(): Boolean {
            return try {
                val response: Response = window.fetch("https://www.google.com/generate_204").await()
                response.status.toInt() == 204 || response.status.toInt() == 200
            } catch (e: Exception) {
                false
            }
        }

        // Initial state
        value = if (window.navigator.onLine) NetworkStatus.Available else NetworkStatus.NoInternet

        val onlineListener: (Event) -> Unit = {
            value = NetworkStatus.Available
        }
        val offlineListener: (Event) -> Unit = {
            value = NetworkStatus.NoInternet
        }

        window.addEventListener("online", onlineListener)
        window.addEventListener("offline", offlineListener)

        // Periodically verify real connectivity (not just "onLine" flag)
        while (true) {
            if (window.navigator.onLine) {
                val connected = checkInternet()
                value = if (connected) NetworkStatus.Available else NetworkStatus.NoInternet
            } else {
                value = NetworkStatus.NoInternet
            }
            delay(500)
        }

        awaitDispose {
            window.removeEventListener("online", onlineListener)
            window.removeEventListener("offline", offlineListener)
        }
    }
}