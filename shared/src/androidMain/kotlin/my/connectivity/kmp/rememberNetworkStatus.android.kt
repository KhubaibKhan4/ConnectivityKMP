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
@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("Missing Permission")
@Composable
actual fun rememberNetworkStatus(): State<Boolean> {
    val context = LocalContext.current

    return produceState(true)  {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val callBack = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                value = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                value = false
            }

            override fun onUnavailable() {
                super.onUnavailable()
                value = false
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
            }

            override fun onLinkPropertiesChanged(
                network: Network,
                linkProperties: LinkProperties
            ) {
                super.onLinkPropertiesChanged(network, linkProperties)
            }

            override fun onBlockedStatusChanged(
                network: Network,
                blocked: Boolean
            ) {
                super.onBlockedStatusChanged(network, blocked)
            }
        }

        cm.registerDefaultNetworkCallback(callBack)

        awaitDispose {
            cm.unregisterNetworkCallback(callBack)
        }
    }

}