package com.github.abraga.verdandi.api.exception

open class VerdandiException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
