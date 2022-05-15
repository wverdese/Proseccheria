package me.wverdese.proseccheria.repo

import app.cash.turbine.test
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.MockSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.wverdese.proseccheria.model.Food
import me.wverdese.proseccheria.model.Other
import me.wverdese.proseccheria.model.Wine
import me.wverdese.proseccheria.model.createTables
import me.wverdese.proseccheria.model.tables
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

@ExperimentalSettingsApi
@ExperimentalCoroutinesApi
class TableDataRepositoryTest {
    private val testCoroutineDispatcher = UnconfinedTestDispatcher()

    private lateinit var repo: TableDataRepository

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                module {
                    single { MockSettings().toFlowSettings(testCoroutineDispatcher) }
                }
            )
        }

        repo = TableDataRepository(
            tables = createTables(2),
            menu = listOf(
                Food(id = "FA-01", type = Food.Type.Antipasto, name = "Tagliere salumi e formaggi"),
                Wine(id = "WP-01", type = Wine.Type.Prosecco, vessel = Wine.Vessel.BOTH, name = "Valdobbiadene Ex Dry"),
                Other(id = "SC-01", type = Other.Type.Spirit, name = "Aperol Spritz"),
            )
        )
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test select table`() = runTest(context = testCoroutineDispatcher) {
        repo.selectedTable.test {
            assertEquals("T-01", awaitItem().id)
            repo.selectTable("T-02")
            assertEquals("T-02", awaitItem().id)
        }
    }
}
