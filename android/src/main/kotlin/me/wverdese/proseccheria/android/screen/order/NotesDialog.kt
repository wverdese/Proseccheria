package me.wverdese.proseccheria.android.screen.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import me.wverdese.proseccheria.domain.TableData
import me.wverdese.proseccheria.model.NotesType

@Composable
fun NotesDialog(
    item: TableData.Item,
    onConfirmClicked: (item: TableData.Item, note: NotesType?) -> Unit,
    onDismiss: () -> Unit,
) {
    val textState = remember { mutableStateOf(item.notes ?: "") }

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
                Text(text = item.item.name, style = MaterialTheme.typography.body2)

                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .weight(weight = 1f, fill = false)
                        .padding(vertical = 16.dp),
                    label = { Text(text = "Note") },
                    value = textState.value,
                    onValueChange = { textState.value = it },
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
                        onConfirmClicked(item, textState.value.trim().takeIf { it.isNotBlank() })
                        onDismiss()
                    }) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}
