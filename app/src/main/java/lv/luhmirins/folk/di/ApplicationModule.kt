package lv.luhmirins.folk.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = []
)
@InstallIn(SingletonComponent::class)
object ApplicationModule {
}
