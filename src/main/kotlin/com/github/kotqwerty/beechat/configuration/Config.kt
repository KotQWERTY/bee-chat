package com.github.kotqwerty.beechat.configuration

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val checkForUpdates: Boolean = true,
    val enableLegacyFormatter: Boolean = false,
    val chat: ChatConfig = ChatConfig(),
    val tabList: TabListConfig = TabListConfig(),
    val messages: MessagesConfig = MessagesConfig(),
)
