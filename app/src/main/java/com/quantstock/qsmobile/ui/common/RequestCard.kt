package com.quantstock.qsmobile.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quantstock.qsmobile.api.Request
import com.quantstock.qsmobile.api.RequestStatus

@Composable
fun RequestCard(request: Request) {
    val backgroundColor = when (request.status) {
        RequestStatus.APPROVED -> MaterialTheme.colorScheme.primaryContainer
        RequestStatus.REJECTED -> MaterialTheme.colorScheme.errorContainer
        RequestStatus.PENDING -> MaterialTheme.colorScheme.surfaceVariant
        RequestStatus.RETURNED -> MaterialTheme.colorScheme.background
    }

    Surface(
        color = backgroundColor,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Requested by: ${request.user.username}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Status: ${request.status}", style = MaterialTheme.typography.bodyMedium)
            if(request.status == RequestStatus.APPROVED) {
                Text(text = "Approved by: ${request.approved_by?.username}", style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = "Note: ${request.note ?: "—"}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            request.items.forEach { item ->
                Text("• ${item.equipment.name} (${item.borrowFrom} → ${item.borrowTo})",
                    style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}