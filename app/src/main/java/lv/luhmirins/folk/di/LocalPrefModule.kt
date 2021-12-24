package lv.luhmirins.folk.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
object LocalPrefModule {

    @Provides
    @Named("localPrefs")
    fun getLocalPrefs(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences("localPrefs", Context.MODE_PRIVATE)
}