package com.kareem.nagwa_task.di.modules

import com.kareem.nagwa_task.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector(modules = [DataModule::class])
    abstract fun bindMainActivity(): MainActivity
}