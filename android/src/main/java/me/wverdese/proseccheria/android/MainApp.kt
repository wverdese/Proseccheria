package me.wverdese.proseccheria.android

import android.app.Application
import me.wverdese.proseccheria.initKoin
import org.koin.dsl.module

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(
            module {

            }
        )
    }
}
