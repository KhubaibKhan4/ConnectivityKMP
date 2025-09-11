package my.connectivity.kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberNetworkStatus(): State<Boolean> {
    return produceState(initialValue = false) {
        val monitor = nw_path_monitor_create()

        nw_path_monitor_set_update_handler(monitor) { path ->
            value = nw_path_get_status(path) == nw_path_status_satisfied
        }


        nw_path_monitor_set_queue(monitor, dispatch_get_main_queue())
        nw_path_monitor_start(monitor)

        awaitDispose {
            nw_path_monitor_cancel(monitor)
        }
    }
}