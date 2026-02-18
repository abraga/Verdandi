package com.github.abraga.verdandi.api.exception

class VerdandiInputException(
    message: String,
    cause: Throwable? = null
) : VerdandiException(message, cause)
