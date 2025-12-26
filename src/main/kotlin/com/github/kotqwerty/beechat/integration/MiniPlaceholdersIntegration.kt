package com.github.kotqwerty.beechat.integration

import io.github.miniplaceholders.api.MiniPlaceholders
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

object MiniPlaceholdersIntegration : PluginIntegration {
    override val pluginName = "MiniPlaceholders"

    val audiencePlaceholders: TagResolver
        get() = if (isAvailable) {
            MiniPlaceholders.audiencePlaceholders()
        } else {
            TagResolver.empty()
        }

    val globalPlaceholders: TagResolver
        get() = if (isAvailable) {
            MiniPlaceholders.globalPlaceholders()
        } else {
            TagResolver.empty()
        }
}
