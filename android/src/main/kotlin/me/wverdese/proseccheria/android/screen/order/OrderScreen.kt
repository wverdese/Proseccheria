@file:OptIn(ExperimentalMaterialApi::class)

package me.wverdese.proseccheria.android.screen.order

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.TextButton
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import me.wverdese.proseccheria.android.R
import me.wverdese.proseccheria.android.screen.AlphaIcon
import me.wverdese.proseccheria.android.screen.AlphaMedium
import me.wverdese.proseccheria.android.theme.AppTheme
import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.domain.TableData.Item.FoodItem
import me.wverdese.proseccheria.model.BOTTLE
import me.wverdese.proseccheria.model.Food
import me.wverdese.proseccheria.model.GLASS
import me.wverdese.proseccheria.model.NotesType
import me.wverdese.proseccheria.model.Other
import me.wverdese.proseccheria.model.QuantityType
import me.wverdese.proseccheria.model.Table
import me.wverdese.proseccheria.model.VesselType
import me.wverdese.proseccheria.model.Wine
import me.wverdese.proseccheria.model.createTables
import me.wverdese.proseccheria.model.tables
import org.koin.androidx.compose.getViewModel

@Composable
fun OrderScreen(viewModel: OrderScreenViewModel = getViewModel()) {
    OrderScreen(
        state = viewModel.state,
        onToggleMode = viewModel::toggleMode,
        onSelectTable = viewModel::selectTable,
        onClearTable = viewModel::clearTable,
        onEditNotes = viewModel::editNotes,
        onIncrementQuantity = viewModel::incrementQuantity,
        onDecrementQuantity = viewModel::decrementQuantity,
        onChangeVessel = viewModel::changeVessel,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrderScreen(
    state: OrderScreenState,
    onToggleMode: () -> Unit,
    onSelectTable: (table: Table) -> Unit,
    onClearTable: (table: Table) -> Unit,
    onEditNotes: (item: TableData.Item, NotesType?) -> Unit,
    onIncrementQuantity: (item: TableData.Item) -> Unit,
    onDecrementQuantity: (item: TableData.Item) -> Unit,
    onChangeVessel: (item: TableData.Item.WineItem) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
    val clickedItemState = remember { mutableStateOf<TableData.Item?>(null) }
    val deleteTableState = remember { mutableStateOf<Table?>(null) }

    clickedItemState.value?.also {
        NotesDialog(
            item = it,
            onConfirmClicked = onEditNotes,
            onDismiss = { clickedItemState.value = null }
        )
    }

    deleteTableState.value?.also {
        ConfirmDialog(
            table = it,
            onConfirmClicked = { onClearTable(it) },
            onDismiss = { deleteTableState.value = null }
        )
    }

    with(state) {
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
                        val isChecked = mode is OrderScreenState.Mode.View
                        IconToggleButton(
                            checked = isChecked,
                            onCheckedChange = { onToggleMode() }
                        ) {
                            val tint by animateColorAsState(
                                if (isChecked) MaterialTheme.colors.primary
                                else MaterialTheme.colors.onSurface
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_wysiwyg),
                                contentDescription = "View Order",
                                tint = tint
                            )
                        }
                        IconButton(onClick = { deleteTableState.value = table }, enabled = isClearTableButtonEnabled) {
                            AlphaIcon(enabled = isClearTableButtonEnabled) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Clear Table"
                                )
                            }
                        }
                    },
                )
            },
            backLayerBackgroundColor = MaterialTheme.colors.surface,
            backLayerContent = {
                LazyColumn {
                    items(count = tables.size) { index ->
                        val table = tables[index].table
                        Box(
                            modifier = Modifier
                                .clickable(onClick = {
                                    onSelectTable(table)
                                    coroutineScope.launch {
                                        scaffoldState.conceal()
                                    }
                                })
                                .fillMaxWidth()
                                .padding(all = 16.dp)
                        ) {
                            val tint = if (tables[index].hasStoredData)
                                MaterialTheme.colors.primary
                            else
                                MaterialTheme.colors.onSurface
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = table.name,
                                style = MaterialTheme.typography.h6,
                                color = tint,
                            )
                        }
                    }
                }
            },
            frontLayerContent = {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    LazyColumn {
                        when (mode) {
                            is OrderScreenState.Mode.Edit -> {
                                mode.groupedItems.forEach { (section, rows) ->
                                    stickyHeader {
                                        Surface(
                                            modifier = Modifier.fillMaxWidth(),
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
                                        val row = rows[index]
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable(onClick = { clickedItemState.value = row }),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .padding(all = 16.dp)
                                                    .weight(1f)
                                            ) {
                                                Text(
                                                    text = row.item.name,
                                                    style = MaterialTheme.typography.body1,
                                                )
                                                row.notes?.let {
                                                    AlphaMedium {
                                                        Text(
                                                            modifier = Modifier.padding(top = 4.dp),
                                                            text = it,
                                                            style = MaterialTheme.typography.body2,
                                                        )
                                                    }
                                                }
                                            }
                                            if (row is TableData.Item.WineItem) {
                                                VesselWidget(
                                                    canChangeVessel = row.canChangeVessel,
                                                    vessel = row.vessel,
                                                    onVesselClick = { onChangeVessel(row) }
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                            }
                                            QuantityWidget(
                                                modifier = Modifier.padding(top = 4.dp),
                                                quantity = row.quantity,
                                                onAddClick = { onIncrementQuantity(row) },
                                                onRemoveClick = { onDecrementQuantity(row) },
                                            )
                                        }
                                    }
                                }
                            }
                            is OrderScreenState.Mode.View -> {
                                items(
                                    count = mode.orders.size + 1,
                                    key = { index -> if (index == 0) "$$" else mode.orders[index - 1].id },
                                ) { index ->
                                    if (index == 0) {
                                        Text(
                                            modifier = Modifier.padding(16.dp),
                                            text = "Order details",
                                            style = MaterialTheme.typography.h6,
                                        )
                                    } else {
                                        val order = mode.orders[index - 1]
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .padding(all = 16.dp)
                                                    .weight(1f)
                                            ) {
                                                Text(
                                                    text = order.text,
                                                    style = MaterialTheme.typography.body1,
                                                )
                                                order.notes?.let {
                                                    AlphaMedium {
                                                        Text(
                                                            modifier = Modifier.padding(top = 4.dp),
                                                            text = it,
                                                            style = MaterialTheme.typography.body2,
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
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
fun QuantityWidget(
    modifier: Modifier = Modifier,
    quantity: QuantityType,
    onAddClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {},
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(modifier = Modifier.size(36.dp), onClick = { onRemoveClick() }) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_remove),
                contentDescription = "Decrement",
            )
        }
        Text(
            modifier = Modifier.padding(bottom = 2.dp, end = 2.dp),
            text = quantity.toString(),
            fontSize = 18.sp,
        )
        IconButton(modifier = Modifier.size(36.dp), onClick = { onAddClick() }) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_add),
                contentDescription = "Increment",
            )
        }
    }
}

@Composable
fun VesselWidget(
    modifier: Modifier = Modifier,
    canChangeVessel: Boolean,
    vessel: VesselType,
    onVesselClick: () -> Unit = {},
) {
    IconButton(
        modifier = modifier.size(36.dp),
        enabled = canChangeVessel,
        onClick = { onVesselClick() }
    ) {
        AlphaIcon(enabled = canChangeVessel) {
            Icon(
                modifier = Modifier.size(if (vessel == GLASS) 24.dp else 32.dp),
                painter = painterResource(if (vessel == GLASS) R.drawable.ic_glass else R.drawable.ic_bottle),
                contentDescription = if (vessel == GLASS) "Glass" else "Bottle",
            )
        }
    }
}

@Composable
fun ConfirmDialog(
    table: Table,
    onConfirmClicked: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.background,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Warning", style = MaterialTheme.typography.h6)
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    text = "You're about to delete all the information stored for ${table.name}. Are you sure?"
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text(text = "Cancel")
                    }
                    TextButton(onClick = {
                        onConfirmClicked()
                        onDismiss()
                    }) {
                        Text(text = "Proceed")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun VesselWidgetPreview() {
    AppTheme {
        Column {
            VesselWidget(canChangeVessel = true, vessel = GLASS)
            VesselWidget(canChangeVessel = true, vessel = BOTTLE)
            VesselWidget(canChangeVessel = false, vessel = GLASS)
            VesselWidget(canChangeVessel = false, vessel = BOTTLE)
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
fun OrderScreenEditPreview() {
    AppTheme {
        OrderScreen(
            state = OrderScreenState(
                tables = createTables(16).mapIndexed { index, table ->
                    TableData.TableItem(table, index % 2 == 0)
                },
                table = tables.first(),
                isClearTableButtonEnabled = true,
                mode = OrderScreenState.Mode.Edit(
                    groupedItems = mapOf(
                        "Antipasti" to listOf(
                            FoodItem(
                                item = Food(
                                    id = "FA-01",
                                    type = Food.Type.Antipasto,
                                    name = "Tagliere salumi e formaggi"
                                ),
                                quantity = 1,
                                notes = "test",
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
                                vessel = GLASS,
                            ),
                            TableData.Item.WineItem(
                                item = Wine(
                                    id = "WP-04",
                                    type = Wine.Type.Prosecco,
                                    vessel = Wine.Vessel.BOTTLE,
                                    name = "Mont Blanc Cuvee Ex Dry"
                                ),
                                quantity = 0,
                                notes = null,
                                vessel = BOTTLE,
                            ),
                        ),
                        "Altro" to listOf(
                            TableData.Item.OtherItem(
                                item = Other(
                                    id = "SC-01",
                                    type = Other.Type.Spirit,
                                    name = "Aperol Spritz"
                                ),
                                quantity = 3,
                                notes = "test test",
                            ),
                            TableData.Item.OtherItem(
                                item = Other(
                                    id = "SC-01",
                                    type = Other.Type.Cafeteria,
                                    name = "Caffè"
                                ),
                                quantity = 0,
                                notes = null,
                            )
                        )
                    )
                ),
            ),
            onSelectTable = {},
            onToggleMode = {},
            onClearTable = {},
            onEditNotes = { _, _ -> },
            onIncrementQuantity = {},
            onDecrementQuantity = {},
            onChangeVessel = {},
        )
    }
}

@Preview
@Composable
fun OrderScreenViewPreview() {
    AppTheme {
        OrderScreen(
            state = OrderScreenState(
                tables = createTables(16).mapIndexed { index, table ->
                    TableData.TableItem(table, index % 2 == 0)
                },
                table = tables.first(),
                isClearTableButtonEnabled = true,
                mode = OrderScreenState.Mode.View(
                    orders = listOf(
                        Order("WP-01", "Mont Blanc Cuvee Ex Dry (glass)", "notes"),
                        Order("SC-01", "Aperol Spritz", null),
                    )
                ),
            ),
            onSelectTable = {},
            onToggleMode = {},
            onClearTable = {},
            onEditNotes = { _, _ -> },
            onIncrementQuantity = {},
            onDecrementQuantity = {},
            onChangeVessel = {},
        )
    }
}

private suspend fun BackdropScaffoldState.toggle() {
    if (isConcealed) reveal() else conceal()
}
