package ru.rusatom.dev.digital.water.media.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import ru.rusatom.dev.digital.water.media.data.repository.MediaRepositoryImpl
import ru.rusatom.dev.digital.water.media.repository.MediaRepository
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@InstallIn(SingletonComponent::class)
@Module
class MediaModule {

    @Provides
    @Singleton
    fun provideRepositoryMedia(
        application: Application,
        coroutineContext: CoroutineContext
    ): MediaRepository {
        return MediaRepositoryImpl(
            application,
            coroutineContext
        )
    }

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineContext {
        return Dispatchers.IO
    }

}