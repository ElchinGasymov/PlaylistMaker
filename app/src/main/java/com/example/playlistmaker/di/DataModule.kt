package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.ApiConstants
import com.example.playlistmaker.Constants
import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.media_library.data.db.TrackDbConvertor
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

val dataModule = module {

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get<OkHttpClient>())
            .build()
    }

    factory<SongsApiService> {
        get<Retrofit>().create(SongsApiService::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(
                Constants.HISTORY_KEY,
                Context.MODE_PRIVATE
            )
    }

    factory { Gson() }

    factory<NetworkClient> {
        RetrofitNetworkClient(
            context = androidContext(),
            service = get()
        )
    }

    single<ILocalStorage> {
        HistoryLocalStorage(
            get(),
            get(),
            get()
        )
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

    factory<SongConverter> {
        SongConverter(convertor = get())
    }

    factory<DurationConverter> {
        DurationConverter()
    }

    factory<MediaPlayer> { MediaPlayer() }

    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    factory<TrackDbConvertor> { TrackDbConvertor() }
}
