package org.intelehealth.common.helper

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHelper @Inject constructor(@ApplicationContext private val context: Context) {

    @SuppressLint("ObsoleteSdkInt")
    fun isNetworkConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork?.let {
                connectivityManager.getNetworkCapabilities(it)?.let { netCapabilities ->
                    hasActiveNetwork(netCapabilities)
                } ?: false
            } ?: false
        } else {
            @Suppress("DEPRECATION")
            connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true
        }
    }

    private fun hasActiveNetwork(networkCapabilities: NetworkCapabilities): Boolean {
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR
        ) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}
