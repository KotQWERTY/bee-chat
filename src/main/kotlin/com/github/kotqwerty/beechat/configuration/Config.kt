package com.github.kotqwerty.beechat.configuration

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val checkForUpdates: Boolean = true,
    val legacyFormatter: LegacyFormatterConfig = LegacyFormatterConfig(),
    val chat: ChatConfig = ChatConfig(),
    val tabList: TabListConfig = TabListConfig(),
    val messages: MessagesConfig = MessagesConfig(),
)
