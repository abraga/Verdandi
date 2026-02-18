package com.github.abraga.verdandi.sampleapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.github.abraga.verdandi.sampleapp.ui.VerdandiShowcaseScreen
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        VerdandiShowcaseScreen()
    }
}
