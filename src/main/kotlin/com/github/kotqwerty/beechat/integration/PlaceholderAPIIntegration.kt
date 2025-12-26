package com.github.kotqwerty.beechat.integration

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player

object PlaceholderAPIIntegration : PluginIntegration {
    override val pluginName = "PlaceholderAPI"

    fun parsePlaceholders(player: Player, text: String) = if (isAvailable) {
        PlaceholderAPI.setPlaceholders(player, text)
    } else {
        text
    }
}
