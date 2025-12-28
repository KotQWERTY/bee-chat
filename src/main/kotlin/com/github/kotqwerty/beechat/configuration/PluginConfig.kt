package com.github.kotqwerty.beechat.configuration

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class PluginConfig(
    val checkForUpdates: Boolean = true,
    val chat: ChatConfig = ChatConfig(),
    val tabList: TabListConfig = TabListConfig(),
    val messages: MessagesConfig = MessagesConfig(),
)
