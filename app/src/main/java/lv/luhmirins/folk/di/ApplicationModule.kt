package lv.luhmirins.folk.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lv.luhmirins.folk.BuildConfig
import javax.inject.Named

@Module(
    includes = [
        ApiModule::class,
        UtilitiesModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Named("enableLogging")
    fun enableLogging(): Boolean = BuildConfig.DEBUG

}
