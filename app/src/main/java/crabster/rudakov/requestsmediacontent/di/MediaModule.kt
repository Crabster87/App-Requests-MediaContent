package crabster.rudakov.requestsmediacontent.di

import android.app.Application
import crabster.rudakov.requestsmediacontent.repository.MediaRepository
import crabster.rudakov.requestsmediacontent.repository.MediaRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@InstallIn(SingletonComponent::class)
@Module
class MediaModule {

    @Provides
    @Singleton
    fun provideRepositoryMedia(
        context: Application,
        coroutineContext: CoroutineContext
    ): MediaRepository {
        return MediaRepositoryImpl(context, coroutineContext)
    }

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineContext {
        return Dispatchers.IO
    }

}