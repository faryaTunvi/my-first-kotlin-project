package com.fattyleo.defanceshotokankarate.di

import com.fattyleo.defanceshotokankarate.DefaultNavigator
import com.fattyleo.defanceshotokankarate.Destination
import com.fattyleo.defanceshotokankarate.Navigator
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<Navigator> {
        DefaultNavigator(startDestination = Destination.AuthGraph)
    }
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::DetailViewModel)
}