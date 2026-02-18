package com.github.abraga.verdandi.api.exception

class VerdandiStateException(
    message: String,
    cause: Throwable? = null
) : VerdandiException(message, cause)
