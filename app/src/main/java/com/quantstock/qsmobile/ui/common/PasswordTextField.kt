package com.quantstock.qsmobile.ui.common

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.quantstock.qsmobile.R

// a PasswordTextField which allows the user to press the button to see their password
// surprised that it's not native to Compose but oh well
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // when the user's default input app allows it (SwiftKey's implementation is REALLY nice)
        modifier = modifier
    )
}