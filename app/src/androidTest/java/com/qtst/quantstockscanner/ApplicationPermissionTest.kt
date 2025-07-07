package com.qtst.quantstockscanner

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import androidx.test.rule.GrantPermissionRule
import org.junit.Test

class ApplicationPermissionTest {
    @get:Rule
    val permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun cameraComposable_showsWhenPermissionGranted() {
        composeTestRule.onNodeWithTag("CameraPreview").assertIsDisplayed()
    }
}