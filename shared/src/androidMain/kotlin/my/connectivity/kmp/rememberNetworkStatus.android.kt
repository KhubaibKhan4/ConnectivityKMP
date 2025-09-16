package my.connectivity.kmp

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import my.connectivity.kmp.data.model.NetworkStatus

@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("MissingPermission")
@Composable
actual fun rememberNetworkStatus(): State<NetworkStatus> {
    val context = LocalContext.current

    return produceState<NetworkStatus>(NetworkStatus.Unavailable) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callBack = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                value = NetworkStatus.NoInternet
            }

            override fun onLost(network: Network) {
                value = NetworkStatus.Lost
            }

            override fun onUnavailable() {
                value = NetworkStatus.Unavailable
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                val validated = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

                value = when {
                    hasInternet && validated -> NetworkStatus.Available
                    hasInternet && !validated -> NetworkStatus.NoInternet
                    else -> NetworkStatus.Unavailable
                }
            }
        }

        cm.registerDefaultNetworkCallback(callBack)

        awaitDispose {
            cm.unregisterNetworkCallback(callBack)
        }
    }
}