@file:OptIn(ExperimentalSettingsApi::class)

package me.wverdese.proseccheria

import com.russhwolf.settings.ExperimentalSettingsApi
import me.wverdese.proseccheria.repo.TableDataRepository
import org.koin.core.Koin
import org.koin.dsl.module

actual val platformModule = module {}

fun Koin.getTableDataRepository(): TableDataRepository = get(TableDataRepository::class)
