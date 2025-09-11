package my.connectivity.kmp

import androidx.compose.runtime.produceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL

@androidx.compose.runtime.Composable
actual fun rememberNetworkStatus(): androidx.compose.runtime.State<Boolean> {
    return produceState(initialValue = true) {
        val checkIntervalMillis = 5000L
        val retryAttempts = 3
        val retryDelayMillis = 1000L

        while (true) {
            val isCurrentlyConnected = withContext(Dispatchers.IO) {
                var connected = false
                var attempts = 0
                while (attempts < retryAttempts) {
                    try {
                        val url = URL("https://www.google.com/generate_204")
                        val connection = url.openConnection() as HttpURLConnection
                        connection.connectTimeout = 1000
                        connection.readTimeout = 1000
                        connection.requestMethod = "HEAD"
                        val responseCode = connection.responseCode
                        connection.disconnect()
                        connected = (responseCode == HttpURLConnection.HTTP_NO_CONTENT || responseCode == HttpURLConnection.HTTP_OK)
                        if (connected) {
                            break
                        }
                    } catch (e: Exception) {
                        println("Network check attempt ${attempts + 1} error: ${e.message}")
                    }
                    attempts++
                    if (!connected && attempts < retryAttempts) {
                        delay(retryDelayMillis)
                    }
                }
                connected
            }

            // Only update the state if it has changed to avoid unnecessary recompositions
            if (value != isCurrentlyConnected) {
                value = isCurrentlyConnected
                println("Network Status changed to: $isCurrentlyConnected")
            }
            delay(checkIntervalMillis) // Wait for the main interval
        }
    }
}