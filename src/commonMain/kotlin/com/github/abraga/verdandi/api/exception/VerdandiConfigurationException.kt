package com.github.abraga.verdandi.api.exception

class VerdandiConfigurationException(
    message: String,
    cause: Throwable? = null
) : VerdandiException(message, cause)
