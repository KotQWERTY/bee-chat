package com.github.kotqwerty.beechat.configuration

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class SpyConfig(
    val format: String = "<aqua>Spy</aqua> <channel_message>",
    val disableOnLeave: Boolean = true,
)
