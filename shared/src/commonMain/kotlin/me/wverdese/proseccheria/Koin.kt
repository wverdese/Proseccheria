package me.wverdese.proseccheria

import me.wverdese.proseccheria.repo.TableDataRepository
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(onStart: KoinApplication.() -> Unit, appModule: Module): KoinApplication =
    startKoin {
        onStart()
        modules(
            appModule,
            platformModule,
            coreModule
        )
    }

@Suppress("unused") // Called in iOS.
fun initKoinIos(): KoinApplication = initKoin(onStart = {}, appModule = module {})

private val coreModule = module {
    single { TableDataRepository() }
}

expect val platformModule: Module
