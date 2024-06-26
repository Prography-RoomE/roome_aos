package com.sevenstars.data.interceptor

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.sevenstars.data.utils.LoggerUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketException
import javax.inject.Inject

class NoConnectionInterceptor @Inject constructor(
    private val context: Application
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        return try {
            if(!isConnectionOn()){
                throw NoConnectivityException()
            } else {
                chain.proceed(builder.build())
            }
        } catch (e: SocketException){
            chain.proceed(builder.build())
        }
    }

    private fun postAndroidMInternetCheck(
        connectivityManager: ConnectivityManager
    ): Boolean {
        val network = connectivityManager.activeNetwork
        val connection = connectivityManager.getNetworkCapabilities(network)

        return connection != null && (
                connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    private fun isConnectionOn(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        return postAndroidMInternetCheck(connectivityManager)
    }

    class NoConnectivityException : IOException() {
        val code = 0

        override val message: String
            get() = "인터넷 연결이 끊겼습니다.\nWIFI나 데이터 연결을 확인해주세요"
    }
}