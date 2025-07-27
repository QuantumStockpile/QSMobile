package com.quantstock.qsmobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.quantstock.qsmobile.ui.common.RequestCard
import com.quantstock.qsmobile.viewmodels.RequestsViewModel

@Composable
fun RequestHistoryScreen(
    requestsViewModel: RequestsViewModel = hiltViewModel(),
) {
    val requests by requestsViewModel.requests.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(requests) { req ->
            RequestCard(request = req)
        }
    }

}