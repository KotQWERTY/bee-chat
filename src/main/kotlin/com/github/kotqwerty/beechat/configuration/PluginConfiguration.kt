package com.github.kotqwerty.beechat.configuration

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class PluginConfiguration(
    val checkForUpdates: Boolean = true,
    val enableChat: Boolean = true,
    val enablePlayerList: Boolean = true,
)
