package com.bossdev.automazione

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = false)
@Composable
fun PreviewMyCard() {
    MyCard("Simple description") { }
}

@Composable
fun MyCard(description: String, onClickFn: () -> Unit) {
    Card(
        onClick = onClickFn,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier.size(width = 240.dp, height = 100.dp)
    )
    {
        Text(
            text = description,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}