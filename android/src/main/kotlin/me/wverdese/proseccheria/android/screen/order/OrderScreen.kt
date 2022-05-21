package me.wverdese.proseccheria.android.screen.order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.wverdese.proseccheria.android.theme.Black
import me.wverdese.proseccheria.android.theme.OnSurfaceDark
import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.domain.TableData.Item.FoodItem
import me.wverdese.proseccheria.domain.TableData.Item.OtherItem
import me.wverdese.proseccheria.domain.TableData.Item.WineItem
import me.wverdese.proseccheria.model.Food
import me.wverdese.proseccheria.model.GLASS
import me.wverdese.proseccheria.model.Other
import me.wverdese.proseccheria.model.Table
import me.wverdese.proseccheria.model.TableId
import me.wverdese.proseccheria.model.Wine
import me.wverdese.proseccheria.model.createTables
import me.wverdese.proseccheria.model.tables
import org.koin.androidx.compose.getViewModel

@Composable
fun OrderScreen(viewModel: OrderScreenViewModel = getViewModel()) {
    val scaffoldState = rememberScaffoldState()

    OrderScreen(
        scaffoldState = scaffoldState,
        state = viewModel.state,
        onTableSelected = { viewModel.selectTable(it) }
    )
}

@Composable
fun OrderScreen(
    scaffoldState: ScaffoldState,
    state: OrderScreenState,
    onTableSelected: (TableId) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    TableSelection(state.tableData.table, state.tables, onTableSelected)
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Delete, tint = MaterialTheme.colors.onSurface, contentDescription = "Clear Table")
                    }
                },
            )
        },
        content = {

        }
    )
}

@Composable
fun TableSelection(selected: Table, tables: List<Table>, onSelected: (TableId) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            Modifier
                .padding(24.dp)
                .clickable {
                    expanded = !expanded
                }
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selected.name, modifier = Modifier.padding(end = 8.dp))
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Table Dropdown")

            DropdownMenu(expanded = expanded, onDismissRequest = {
                expanded = false
            }) {
                tables.forEach { table ->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onSelected(table.id)
                    }) {
                        Text(text = table.name)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun OrderScreenPreview() {
    OrderScreen(
        scaffoldState = rememberScaffoldState(),
        state = OrderScreenState(
            tables = createTables(1),
            tableData = TableData(
                table = tables.first(),
                items = listOf(
                    FoodItem(
                        item = Food(
                            id = "FA-01",
                            type = Food.Type.Antipasto,
                            name = "Tagliere salumi e formaggi"
                        ),
                        quantity = 1,
                        notes = "test"
                    ),
                    WineItem(
                        item = Wine(
                            id = "WP-01",
                            type = Wine.Type.Prosecco,
                            vessel = Wine.Vessel.BOTH, name = "Valdobbiadene Ex Dry"
                        ),
                        quantity = 2,
                        notes = "test",
                        vessel = GLASS
                    ),
                    OtherItem(
                        item = Other(
                            id = "SC-01",
                            type = Other.Type.Spirit,
                            name = "Aperol Spritz"
                        ),
                        quantity = 3,
                        notes = "test test"
                    )
                )
            )
        ),
        onTableSelected = {}
    )
}
