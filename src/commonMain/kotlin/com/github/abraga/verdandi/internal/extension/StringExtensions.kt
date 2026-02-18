package com.github.abraga.verdandi.internal.extension

fun String.capitalized(): String {
    return lowercase().replaceFirstChar { it.uppercase() }
}
