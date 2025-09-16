package my.connectivity.kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import my.connectivity.kmp.data.model.NetworkStatus
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL

@Composable
actual fun rememberNetworkStatus(): State<NetworkStatus> {
    return produceState<NetworkStatus>(initialValue = NetworkStatus.Unavailable) {
        val checkIntervalMillis = 5000L       // run every 5s
        val retryAttempts = 3                 // retry attempts per cycle
        val retryDelayMillis = 500L          // 1s between retries

        while (true) {
            val status = withContext(Dispatchers.IO) {
                var attempts = 0
                var success = false
                var totalLatency = 0L

                while (attempts < retryAttempts) {
                    try {
                        val url = URL("https://www.google.com/generate_204")
                        val start = System.currentTimeMillis()

                        val connection = (url.openConnection() as HttpURLConnection).apply {
                            connectTimeout = 1000
                            readTimeout = 1000
                            requestMethod = "HEAD"
                        }

                        val responseCode = connection.responseCode
                        val latency = System.currentTimeMillis() - start
                        connection.disconnect()

                        if (responseCode == HttpURLConnection.HTTP_NO_CONTENT ||
                            responseCode == HttpURLConnection.HTTP_OK
                        ) {
                            success = true
                            totalLatency += latency
                            break
                        }
                    } catch (e: Exception) {
                        println("Network check attempt ${attempts + 1} failed: ${e.message}")
                    }
                    attempts++
                    if (!success && attempts < retryAttempts) {
                        delay(retryDelayMillis)
                    }
                }

                when {
                    success -> {
                        val avgLatency = if (totalLatency > 0) totalLatency / (attempts + 1) else 0
                        if (avgLatency > 1500) NetworkStatus.Slow else NetworkStatus.Available
                    }
                    else -> NetworkStatus.NoInternet
                }
            }

            if (value != status) {
                value = status
                println("Network Status changed to: $status")
            }

            delay(checkIntervalMillis)
        }
    }
}