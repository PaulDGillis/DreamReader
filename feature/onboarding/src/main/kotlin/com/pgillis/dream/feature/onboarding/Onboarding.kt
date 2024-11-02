package com.pgillis.dream.feature.onboarding

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pgillis.dream.core.designsystem.theme.DreamReaderTheme
import com.pgillis.dream.core.ui.DevicePreviews

@Composable
fun Onboard() {
    Text("Empty")
}

@DevicePreviews
@Composable
fun OnboardPreview() {
    DreamReaderTheme {
        Onboard()
    }
}
