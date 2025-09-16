package my.connectivity.kmp.data.model

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object NoInternet : NetworkStatus()
    object Lost : NetworkStatus()
    object Unavailable : NetworkStatus()
    object Slow : NetworkStatus()
    object Losing: NetworkStatus()
}
