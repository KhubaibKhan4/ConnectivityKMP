package my.connectivity.kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import kotlinx.cinterop.ExperimentalForeignApi
import my.connectivity.kmp.data.model.NetworkStatus
import platform.Network.nw_path_get_status
import platform.Network.nw_path_is_expensive
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfiable
import platform.Network.nw_path_status_satisfied
import platform.Network.nw_path_status_unsatisfied
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberNetworkStatus(): State<NetworkStatus> {
    return produceState<NetworkStatus>(initialValue = NetworkStatus.Unavailable) {
        val monitor = nw_path_monitor_create()

        nw_path_monitor_set_update_handler(monitor) { path ->
            val status = nw_path_get_status(path)

            value = when (status) {
                nw_path_status_satisfied -> {
                    if (nw_path_is_expensive(path)) {
                        NetworkStatus.Slow
                    } else {
                        NetworkStatus.Available
                    }
                }
                nw_path_status_unsatisfied -> NetworkStatus.NoInternet
                nw_path_status_satisfiable -> NetworkStatus.Lost
                else -> NetworkStatus.Unavailable
            }
        }

        nw_path_monitor_set_queue(monitor, dispatch_get_main_queue())
        nw_path_monitor_start(monitor)

        awaitDispose {
            nw_path_monitor_cancel(monitor)
        }
    }
}