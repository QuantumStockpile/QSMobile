package com.quantstock.qsmobile.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.quantstock.qsmobile.api.Equipment

@Composable
fun ExpandedItemView(
    item: Equipment,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Image (larger)
                    AsyncImage(
                        model = item.photoUrl ?: "https://www.differencebetween.net/wp-content/uploads/2012/01/Difference-Between-Example-and-Sample.jpg",
                        contentDescription = item.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) { // Location
                    Text(
                        text = "Location: ${item.location.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    // Condition
                    Text(
                        text = "Condition: ${item.condition}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    // Metadata or Description (if you have it)
                    item.metadata?.let { metadata ->
                        Text(
                            text = "Description: $metadata",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

            }
        }
    }
}