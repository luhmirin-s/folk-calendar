package lv.luhmirins.folk.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.RealmConfiguration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun getRealmConfig(): RealmConfiguration = RealmConfiguration.Builder()
        .name("calendarRealm")
        .build()

    @Provides
    @Named("realmDispatcher")
    fun ioDispatcher(): CoroutineDispatcher =
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
}
