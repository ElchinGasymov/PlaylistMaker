package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.ApiConstants
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.Response
import com.example.playlistmaker.search.data.SongsSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val context: Context,
    private val service: SongsApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = ApiConstants.NO_INTERNET_CONNECTION_CODE }
        }
        if (dto !is SongsSearchRequest) {
            return Response().apply { resultCode = ApiConstants.BAD_REQUEST_CODE }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = service.getSongs(dto.expression)
                response.apply { resultCode = ApiConstants.SUCCESS_CODE }
            } catch (e: Throwable) {
                Response().apply { resultCode = ApiConstants.INTERNAL_SERVER_ERROR }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
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
