package lv.luhmirins.folk.di

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lv.luhmirins.folk.BuildConfig
import lv.luhmirins.folk.data.api.HolidaysApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Named("apiKey")
    fun apiKey(): String = BuildConfig.apiKey

    @Provides
    @Named("baseUrl")
    fun baseUrl(): String = "https://wozmx9dh26.execute-api.eu-west-1.amazonaws.com/"

    @Provides
    @Named("httpCacheDir")
    fun httpCacheDir(application: Application): File? = application.cacheDir

    @Provides
    @Singleton
    fun okHttpClient(
        @Named("httpCacheDir") cacheDir: File?,
        @Named("enableLogging") enableLogging: Boolean,
    ): OkHttpClient = OkHttpClient.Builder().apply {
        if (cacheDir != null) cache(Cache(cacheDir, (1024 * 1024 * 100).toLong())) // 100MB
        if (enableLogging) addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        )
    }.build()

    @Provides
    fun moshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun apiService(
        @Named("baseUrl") baseUrl: String,
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): HolidaysApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .callFactory(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create()

}