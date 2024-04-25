package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.ApiConstants
import com.example.playlistmaker.Constants
import com.example.playlistmaker.search.data.ILocalStorage
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.SongsApiService
import com.example.playlistmaker.search.data.storage.HistoryLocalStorage
import com.example.playlistmaker.settings.data.LocalStorage
import com.example.playlistmaker.settings.data.preferencies.ThemeLocalStorage
import com.example.playlistmaker.ui.converter.DurationConverter
import com.example.playlistmaker.ui.converter.SongConverter
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val dataModule = module {

    val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    single<SongsApiService> {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create<SongsApiService>()
    }

    single {
        androidContext()
            .getSharedPreferences(
                Constants.HISTORY_KEY,
                Context.MODE_PRIVATE
            )

    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(
            context = androidContext(),
            songsApiService = get()
        )
    }

    single<ILocalStorage> {
        HistoryLocalStorage(get(), get())
    }

    single<SharedPreferences> {
        androidContext()
            .getSharedPreferences(
            Constants.APP_THEME_KEY,
            Context.MODE_PRIVATE
        )
    }

    single<LocalStorage> {
        ThemeLocalStorage(get())
    }

    single<SongConverter> {
        SongConverter(convertor = get())
    }

    single<DurationConverter> {
        DurationConverter()
    }

    //singleOf(::RetrofitNetworkClient).bind<NetworkClient>()
}
