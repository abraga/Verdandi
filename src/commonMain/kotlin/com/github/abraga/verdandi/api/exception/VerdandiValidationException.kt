package com.github.abraga.verdandi.api.exception

class VerdandiValidationException(
    message: String,
    cause: Throwable? = null
) : VerdandiException(message, cause)
