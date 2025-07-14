package com.quantstock.qsmobile.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat.getString
import com.quantstock.qsmobile.R
import com.quantstock.qsmobile.api.*

@Composable
fun ApiInfoScreen(apiService: ApiService, context: Context) {
    var apiInfo by remember { mutableStateOf<ApiInfo?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            apiInfo = apiService.getApiInfo()
        } catch (e: Exception) {
            error = e.message
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        if (apiInfo != null) {
            Text(
                text = "${getString(context, R.string.api_version)}: ${apiInfo!!.api_version}",
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "${getString(context, R.string.build_version)}: ${apiInfo!!.build_version}",
                color = MaterialTheme.colorScheme.onBackground
            )
        } else if (error != null) {
            Text(text = "Error: $error", color = Color.Red)
        } else {
            CircularProgressIndicator()
        }
    }
}