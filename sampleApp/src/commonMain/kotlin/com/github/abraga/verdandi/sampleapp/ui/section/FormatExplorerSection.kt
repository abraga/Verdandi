package com.github.abraga.verdandi.sampleapp.ui.section

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.sampleapp.ui.component.PatternFormatEditor
import com.github.abraga.verdandi.sampleapp.ui.component.ShowcaseSection

@Composable
fun FormatExplorerSection(
    now: VerdandiMoment,
    modifier: Modifier = Modifier
) {
    var formatPattern by remember { mutableStateOf("") }

    ShowcaseSection(title = "Format Explorer", modifier = modifier) {
        PatternFormatEditor(
            pattern = formatPattern,
            referenceMoment = now,
            onPatternChange = { formatPattern = it },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
