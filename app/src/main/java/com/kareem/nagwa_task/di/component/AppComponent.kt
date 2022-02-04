package com.kareem.nagwa_task.di.component

import android.app.Application
import com.kareem.nagwa_task.App
import com.kareem.nagwa_task.di.modules.DataModule
import com.kareem.nagwa_task.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, DataModule::class]
)
interface AppComponent {

    fun inject(app: App)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}