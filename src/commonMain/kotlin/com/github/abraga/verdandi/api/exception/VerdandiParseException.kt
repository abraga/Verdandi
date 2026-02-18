package com.github.abraga.verdandi.api.exception

class VerdandiParseException(
    message: String,
    cause: Throwable? = null
) : VerdandiException(message, cause)
