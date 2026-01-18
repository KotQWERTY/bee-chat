package com.github.kotqwerty.beechat.configuration

import com.github.kotqwerty.beechat.chat.ChatChannel
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class ChatConfiguration(
    val defaultChannel: String = "default",
    val resetChannelOnPlayerQuit: Boolean = true,
    val channels: Map<String, ChatChannel> = mapOf(
        "default" to ChatChannel(
            displayName = "default",
            format = "<player_name> <dark_gray>→</dark_gray> <gray><message></gray>",
        ),
    ),
    val privateMessages: PrivateMessages = PrivateMessages(),
    val spyMode: SpyMode = SpyMode(),
) {
    @ConfigSerializable
    data class PrivateMessages(
        val enable: Boolean = true,
        val command: CommandConfiguration = CommandConfiguration(
            label = "msg",
            aliases = listOf("w", "tell"),
        ),
        val messageIncoming: String = "<player_name> whispers to you: <message>",
        val messageOutgoing: String = "You whisper to <player_name>: <message>",
    )

    @ConfigSerializable
    data class SpyMode(
        val enable: Boolean = true,
        val disableOnPlayerQuit: Boolean = true,
        val enableForPrivateMessages: Boolean = true,
        val format: String = "<aqua>ꜱᴘʏ</aqua> <formatted_message>",
        val channels: Collection<String> = emptyList(),
    )
}
