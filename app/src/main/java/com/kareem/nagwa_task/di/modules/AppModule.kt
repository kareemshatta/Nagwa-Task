package com.kareem.nagwa_task.di.modules

import android.app.Application
import android.content.Context
import com.kareem.nagwa_task.di.qualifiers.IoScheduler
import com.kareem.nagwa_task.di.qualifiers.MainScheduler
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module()
class AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application
    }

    @MainScheduler
    @Provides
    @Singleton
    fun provideMainScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @IoScheduler
    @Provides
    @Singleton
    fun provideIoScheduler(): Scheduler {
        return Schedulers.io()
    }
}