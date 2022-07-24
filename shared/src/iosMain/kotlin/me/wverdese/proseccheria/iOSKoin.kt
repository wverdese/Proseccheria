@file:OptIn(ExperimentalSettingsApi::class, ExperimentalCoroutinesApi::class)

package me.wverdese.proseccheria

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.wverdese.proseccheria.repo.TableDataRepository
import org.koin.core.Koin
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual val platformModule = module {
    single { AppleSettings(NSUserDefaults.standardUserDefaults).toFlowSettings() }
}

fun Koin.getTableDataRepository(): TableDataRepository = get(TableDataRepository::class)
