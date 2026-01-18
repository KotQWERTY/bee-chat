package com.github.kotqwerty.beechat.configuration

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class CommandConfiguration(
    val label: String,
    val description: String? = null,
    val aliases: Collection<String> = emptyList(),
)
