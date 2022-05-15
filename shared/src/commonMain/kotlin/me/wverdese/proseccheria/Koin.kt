package me.wverdese.proseccheria

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(appModule: Module): KoinApplication =
    startKoin {
        modules(
            appModule,
            platformModule,
            coreModule
        )
    }

private val coreModule = module {}

expect val platformModule: Module
