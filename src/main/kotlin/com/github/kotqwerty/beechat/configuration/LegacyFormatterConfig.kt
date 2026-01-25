package com.github.kotqwerty.beechat.configuration

import kotlinx.serialization.Serializable

@Serializable
data class LegacyFormatterConfig(
    val enable: Boolean = false,
    val character: Char = '&',
)
