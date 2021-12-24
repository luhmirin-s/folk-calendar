package lv.luhmirins.folk.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object UtilitiesModule {

    @Provides
    @Named("ioDispatcher")
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Named("apiDateFormat")
    fun dateFormatter(): DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    @Provides
    @Named("prettyDateFormat")
    fun prettyDateFormatter(): DateTimeFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

    @Provides
    @Named("weekDayPrettyDateFormat")
    fun weekDayPrettyDateFormat(): DateTimeFormatter =
        DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy")
}
