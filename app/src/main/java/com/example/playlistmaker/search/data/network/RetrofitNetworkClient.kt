package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.ApiConstants
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.Response
import com.example.playlistmaker.search.data.SongsSearchRequest

class RetrofitNetworkClient(
    private val context: Context,
    private val service: SongsApiService
) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response()
                .apply { resultCode = ApiConstants.NO_INTERNET_CONNECTION_CODE }
        }
        return if (dto is SongsSearchRequest) {
            val resp = service.getSongs(dto.expression).execute()

            val body = resp.body() ?: Response()

            body.apply { resultCode = resp.code() }
        } else {
            Response().apply { resultCode = ApiConstants.BAD_REQUEST_CODE }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

}
