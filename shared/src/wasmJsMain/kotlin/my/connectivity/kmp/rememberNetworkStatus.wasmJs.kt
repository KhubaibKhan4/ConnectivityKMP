package my.connectivity.kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import kotlinx.browser.window
import org.w3c.dom.events.Event

@Composable
actual fun rememberNetworkStatus(): State<Boolean> {
    return produceState(initialValue = window.navigator.onLine) {
        val onlineListener: (Event) -> Unit = { value = true }
        val offlineListener: (Event) -> Unit = { value = false }

        window.addEventListener("online", onlineListener)
        window.addEventListener("offline", offlineListener)

        awaitDispose {
            window.removeEventListener("online", onlineListener)
            window.removeEventListener("offline", offlineListener)
        }
    }
}