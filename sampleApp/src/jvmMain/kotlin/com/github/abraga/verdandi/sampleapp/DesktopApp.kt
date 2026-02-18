package com.github.abraga.verdandi.sampleapp

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.abraga.verdandi.sampleapp.ui.VerdandiShowcaseScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Verdandi Showcase",
        resizable = true,
        state = rememberWindowState(width = 768.dp, height = 1080.dp)
    ) {
        VerdandiShowcaseScreen()
    }
}
