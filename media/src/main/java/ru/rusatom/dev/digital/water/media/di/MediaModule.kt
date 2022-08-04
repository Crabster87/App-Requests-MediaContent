package ru.rusatom.dev.digital.water.media.di

import android.content.ContentResolver
import android.content.Context
import ru.rusatom.dev.digital.water.media.repository.MediaRepository
import ru.rusatom.dev.digital.water.media.data.repository.MediaRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        contentResolver: ContentResolver,
        coroutineContext: CoroutineContext
    ): MediaRepository {
        return MediaRepositoryImpl(
            contentResolver,
            coroutineContext
        )
    }

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideContentResolver(
        @ApplicationContext context: Context
    ): ContentResolver {
        return context.contentResolver
    }

}