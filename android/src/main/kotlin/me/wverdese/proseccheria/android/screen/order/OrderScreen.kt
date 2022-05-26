@file:OptIn(ExperimentalMaterialApi::class)

package me.wverdese.proseccheria.android.screen.order

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.wverdese.proseccheria.android.R
import me.wverdese.proseccheria.android.theme.AppTheme
import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.domain.TableData.Item.FoodItem
import me.wverdese.proseccheria.model.BOTTLE
import me.wverdese.proseccheria.model.Food
import me.wverdese.proseccheria.model.GLASS
import me.wverdese.proseccheria.model.NotesType
import me.wverdese.proseccheria.model.Other
import me.wverdese.proseccheria.model.QuantityType
import me.wverdese.proseccheria.model.TableId
import me.wverdese.proseccheria.model.Wine
import me.wverdese.proseccheria.model.createTables
import me.wverdese.proseccheria.model.tables
import org.koin.androidx.compose.getViewModel

@Composable
fun OrderScreen(viewModel: OrderScreenViewModel = getViewModel()) {
    OrderScreen(
        state = viewModel.state,
        onTableSelected = viewModel::selectTable,
        onNotesChanged = viewModel::editNotes,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrderScreen(
    state: OrderScreenState,
    onTableSelected: (TableId) -> Unit,
    onNotesChanged: (item: TableData.Item, NotesType?) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
    val viewOrder = remember { mutableStateOf(false) }
    val clickedItemState = remember { mutableStateOf<TableData.Item?>(null) }

    with(state) {
        val item = clickedItemState.value
        if (item != null) {
            NotesDialog(
                item = item,
                onConfirmClicked = onNotesChanged,
                onDismiss = { clickedItemState.value = null }
            )
        }

        BackdropScaffold(
            scaffoldState = scaffoldState,
            appBar = {
                TopAppBar(
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.surface,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    scaffoldState.toggle()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Go back",
                            )
                        }
                    },
                    title = {
                        Text(text = table.name)
                    },
                    actions = {
                        IconToggleButton(
                            checked = viewOrder.value,
                            onCheckedChange = { viewOrder.value = it }
                        ) {
                            val tint by animateColorAsState(
                                if (viewOrder.value) MaterialTheme.colors.primary
                                else MaterialTheme.colors.onSurface
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_wysiwyg),
                                contentDescription = "View Order",
                                tint = tint,
                            )
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Filled.Delete,
                                tint = MaterialTheme.colors.onSurface,
                                contentDescription = "Clear Table"
                            )
                        }
                    },
                )
            },
            backLayerBackgroundColor = MaterialTheme.colors.surface,
            backLayerContent = {
                LazyColumn {
                    items(count = tables.size) { index ->
                        Box(
                            modifier = Modifier
                                .clickable(onClick = {
                                    onTableSelected(tables[index].id)
                                    coroutineScope.launch {
                                        scaffoldState.conceal()
                                    }
                                })
                                .fillMaxWidth()
                                .padding(all = 16.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = tables[index].name,
                                style = MaterialTheme.typography.h6,
                            )
                        }
                    }
                }
            },
            frontLayerContent = {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    LazyColumn {
                        groupedItems.forEach { (section, rows) ->
                            stickyHeader {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    color = MaterialTheme.colors.background,
                                ) {
                                    Text(
                                        modifier = Modifier.padding(16.dp),
                                        text = section,
                                        style = MaterialTheme.typography.h6,
                                    )
                                }
                            }
                            items(
                                count = rows.size,
                                key = { index -> rows[index].item.id },
                            ) { index ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(onClick = {
                                            clickedItemState.value = rows[index]
                                        }),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(all = 16.dp)
                                            .weight(1f)
                                    ) {
                                        Text(
                                            text = rows[index].item.name,
                                            style = MaterialTheme.typography.body1,
                                        )
                                        rows[index].notes?.let {
                                            Text(
                                                modifier = Modifier.padding(top = 4.dp),
                                                text = it,
                                                style = MaterialTheme.typography.caption,
                                            )
                                        }
                                    }
                                    QuantityWidget(
                                        modifier = Modifier.padding(top = 4.dp),
                                        quantity = rows[index].quantity,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun QuantityWidget(modifier: Modifier = Modifier, quantity: QuantityType?) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(R.drawable.ic_remove),
                tint = MaterialTheme.colors.onSurface,
                contentDescription = "Decrement",
            )
        }
        Text(
            modifier = Modifier.padding(bottom = 2.dp, end = 2.dp),
            text = "%2d".format(quantity ?: 0),
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(R.drawable.ic_add),
                tint = MaterialTheme.colors.onSurface,
                contentDescription = "Increment",
            )
        }
    }
}

@Preview
@Composable
fun QuantityWidgetPreview() {
    AppTheme {
        QuantityWidget(quantity = 99)
    }
}

@Preview
@Composable
fun OrderScreenPreview() {
    AppTheme {
        OrderScreen(
            state = OrderScreenState(
                tables = createTables(16),
                table = tables.first(),
                groupedItems = mapOf(
                    "Antipasti" to listOf(
                        FoodItem(
                            item = Food(
                                id = "FA-01",
                                type = Food.Type.Antipasto,
                                name = "Tagliere salumi e formaggi"
                            ),
                            quantity = 1,
                            notes = "test"
                        ),
                    ),
                    "Prosecchi" to listOf(
                        TableData.Item.WineItem(
                            item = Wine(
                                id = "WP-01",
                                type = Wine.Type.Prosecco,
                                vessel = Wine.Vessel.BOTH,
                                name = "Valdobbiadene Ex Dry"
                            ),
                            quantity = 2,
                            notes = "test1",
                            vessel = GLASS
                        ),
                        TableData.Item.WineItem(
                            item = Wine(
                                id = "WP-04",
                                type = Wine.Type.Prosecco,
                                vessel = Wine.Vessel.BOTTLE,
                                name = "Mont Blanc Cuvee Ex Dry"
                            ),
                            quantity = null,
                            notes = null,
                            vessel = BOTTLE
                        ),
                    ),
                    "Alcolici" to listOf(
                        TableData.Item.OtherItem(
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
            onTableSelected = {},
            onNotesChanged = { _, _ -> }
        )
    }
}

private suspend fun BackdropScaffoldState.toggle() {
    if (isConcealed) reveal() else conceal()
}
