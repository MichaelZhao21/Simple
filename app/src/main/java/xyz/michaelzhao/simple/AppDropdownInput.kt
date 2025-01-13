package xyz.michaelzhao.simple

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
fun AppDropdownInput(
    text: String,
    onTextChange: (String) -> Unit,
    packages: List<Pair<String, String>>
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text("App Display Number") },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .onFocusEvent {
                    if (it.isFocused) {
                        expanded = true
                    }
                },
            trailingIcon = {
                Icon(
                    icon,
                    "dropdown",
                    modifier = Modifier.clickable { expanded = !expanded })
            },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(textFieldSize.width.dp)
        ) {
            packages.map { d ->
                DropdownMenuItem(
                    text = { Text(d.first) },
                    onClick = {
                        onTextChange(d.first)
                        expanded = false
                    }
                )
            }
        }
    }
}