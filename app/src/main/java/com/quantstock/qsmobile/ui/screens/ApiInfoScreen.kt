package com.quantstock.qsmobile.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import com.quantstock.qsmobile.R
import com.quantstock.qsmobile.api.ApiInfo
import com.quantstock.qsmobile.api.ApiService
import com.quantstock.qsmobile.viewmodels.AuthViewModel

@Composable
fun ApiInfoScreen(apiService: ApiService, context: Context, authViewModel: AuthViewModel) {
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

        Button(
            onClick = {
                authViewModel.logout()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = getString(context, R.string.logout))
        }
    }
}