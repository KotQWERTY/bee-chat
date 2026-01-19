package com.github.kotqwerty.beechat.chat

import com.github.kotqwerty.beechat.configuration.CommandConfiguration
import com.github.kotqwerty.beechat.configuration.SoundConfiguration
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class ChatChannel(
    val displayName: String,
    val format: String,
    val messagePrefix: String = "",
    val command: CommandConfiguration? = null,
    val maxDistance: Int = 0,
    val sound: SoundConfiguration? = null,
    val sendPermission: String = "",
    val receivePermission: String = "",
)
