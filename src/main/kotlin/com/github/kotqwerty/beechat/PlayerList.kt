package com.github.kotqwerty.beechat

import com.github.kotqwerty.beechat.configuration.Configuration
import com.github.kotqwerty.beechat.configuration.PlayerListConfiguration
import com.github.kotqwerty.beechat.integration.MiniPlaceholdersIntegration
import com.github.kotqwerty.beechat.integration.PlaceholderAPIIntegration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerList(private val config: Configuration<PlayerListConfiguration>) {
    fun send(player: Player) {
        val config = config.access()
        val audiencePlaceholders = MiniPlaceholdersIntegration.audiencePlaceholders

        if (config.playerName.isNotEmpty()) {
            val playerName = PlaceholderAPIIntegration.parsePlaceholders(player, config.playerName)

            val tags = TagResolver.resolver(
                Placeholders.name(player.displayName()),
                audiencePlaceholders
            )

            player.playerListName(MiniMessage.miniMessage().deserialize(playerName, player, tags))
        }

        val header = PlaceholderAPIIntegration.parsePlaceholders(player, config.header)
        val footer = PlaceholderAPIIntegration.parsePlaceholders(player, config.footer)

        val placeholders = TagResolver.resolver(
            MiniPlaceholdersIntegration.globalPlaceholders,
            audiencePlaceholders
        )

        player.sendPlayerListHeaderAndFooter(
            MiniMessage.miniMessage().deserialize(header, player, placeholders),
            MiniMessage.miniMessage().deserialize(footer, player, placeholders),
        )
    }

    fun update() {
        Bukkit.getOnlinePlayers().forEach(::send)
    }
}
