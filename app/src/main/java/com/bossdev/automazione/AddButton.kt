package com.bossdev.automazione

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = false)
@Composable
fun PreviewAddButton() {
    AddButton(
        modifier = Modifier.padding(),
        onClick = {}
    )
}

@Composable
fun AddButton(onClick: () -> Unit, modifier: Modifier) {
    FloatingActionButton(
        onClick = { onClick() },
        shape = CircleShape,
        modifier = modifier
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}
