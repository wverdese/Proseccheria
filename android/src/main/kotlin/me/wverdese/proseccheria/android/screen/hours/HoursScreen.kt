package me.wverdese.proseccheria.android.screen.hours

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel

@Composable
fun HoursScreen(viewModel: HoursScreenViewModel = getViewModel()) {
    HoursScreen(
        state = viewModel.state,
        onInputChanged = viewModel::onInputChanged
    )
}

@Composable
fun HoursScreen(
    state: HoursScreenState,
    onInputChanged: (startHours: String, startMinutes: String, endHours: String, endMinutes: String) -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colors.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp)
        ) {
            TimeHeader("Start")
            Row {
                OutlineTimeField(
                    modifier = Modifier.weight(1f),
                    value = state.startHours,
                    isError = state.startHoursError,
                    label = "Hours"
                ) { startHours ->
                    onInputChanged(startHours, state.startMinutes, state.endHours, state.endMinutes)
                }
                OutlineTimeField(
                    modifier = Modifier.weight(1f),
                    value = state.startMinutes,
                    isError = state.startMinutesError,
                    label = "Minutes"
                ) { startMinutes ->
                    onInputChanged(state.startHours, startMinutes, state.endHours, state.endMinutes)
                }
            }
            TimeHeader("End")
            Row {
                OutlineTimeField(
                    modifier = Modifier.weight(1f),
                    value = state.endHours,
                    isError = state.endHoursError,
                    label = "Hours"
                ) { endHours ->
                    onInputChanged(state.startHours, state.startMinutes, endHours, state.endMinutes)
                }
                OutlineTimeField(
                    modifier = Modifier.weight(1f),
                    value = state.endMinutes,
                    isError = state.endMinutesError,
                    label = "Minutes"
                ) { endMinutes ->
                    onInputChanged(state.startHours, state.startMinutes, state.endHours, endMinutes)
                }
            }
            TimeHeader("Total")
            Row {
                OutlineTimeField(
                    modifier = Modifier.weight(1f),
                    value = state.totalHours,
                    label = "Hours",
                    enabled = false
                )
                OutlineTimeField(
                    modifier = Modifier.weight(1f),
                    value = state.totalMinutes,
                    label = "Minutes",
                    enabled = false
                )
            }
        }
    }
}

@Composable
fun TimeHeader(text: String) =
    Box(modifier = Modifier.padding(16.dp)) {
        Text(
            text = text,
            style = MaterialTheme.typography.h6,
        )
    }

@Composable
fun OutlineTimeField(
    modifier: Modifier,
    value: String,
    isError: Boolean = false,
    label: String,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit = {}
) =
    Box(modifier = modifier.padding(horizontal = 18.dp)) {
        OutlinedTextField(
            modifier = modifier,
            value = value,
            isError = isError,
            label = { Text(text = label) },
            singleLine = true,
            enabled = enabled,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = MaterialTheme.colors.onSurface,
                disabledBorderColor = MaterialTheme.colors.onSurface,
                disabledLabelColor = MaterialTheme.colors.onSurface
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            onValueChange = { onValueChange(it) },
        )
    }

@Preview
@Composable
fun HoursScreenPreview() {
    HoursScreen()
}
