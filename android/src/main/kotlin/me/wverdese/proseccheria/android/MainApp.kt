package me.wverdese.proseccheria.android

import android.app.Application
import me.wverdese.proseccheria.android.screen.order.OrderScreen
import me.wverdese.proseccheria.android.screen.order.OrderScreenViewModel
import me.wverdese.proseccheria.initKoin
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(appModule = appModule)
    }
}

private val appModule = module {
    viewModel { OrderScreenViewModel(tableDataRepo = get()) }
}
