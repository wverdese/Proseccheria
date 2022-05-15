@file:OptIn(ExperimentalSettingsApi::class)

package me.wverdese.proseccheria

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.datastore.DataStoreSettings
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "datastore")

@ExperimentalSettingsImplementation
actual val platformModule = module {
    single<FlowSettings> { DataStoreSettings(get<Context>().dataStore) }
}
