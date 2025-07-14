package com.quantstock.qsmobile.ui.common

import androidx.compose.foundation.text.KeyboardOptions
import com.quantstock.qsmobile.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.*
import androidx.compose.ui.text.input.*

@Composable
fun PasswordTextField(
    label: @Composable (() -> Unit)? = null,
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = label,
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                ImageVector.vectorResource(R.drawable.visibility)
            else ImageVector.vectorResource(R.drawable.visibility_off)

            val description = if (passwordVisible) stringResource(R.string.hide_password)
            else stringResource(R.string.show_password)

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = modifier
    )
}