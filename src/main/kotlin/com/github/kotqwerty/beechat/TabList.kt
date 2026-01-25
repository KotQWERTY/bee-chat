package com.github.kotqwerty.beechat

import com.github.kotqwerty.beechat.integration.MiniPlaceholdersIntegration
import com.github.kotqwerty.beechat.integration.PlaceholderAPIIntegration
import com.github.kotqwerty.beechat.utils.LegacyFormatTranslator
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object TabList {
    fun send(player: Player) {
        val tabListConfig = BeeChat.instance.config.tabList
        val audiencePlaceholders = MiniPlaceholdersIntegration.audiencePlaceholders

        if (tabListConfig.playerName.isNotEmpty()) {
            val tabListName = parseFormat(player, tabListConfig.playerName)

            val tags = TagResolver.resolver(
                Placeholders.name(player.displayName()),
                audiencePlaceholders
            )

            player.playerListName(MiniMessage.miniMessage().deserialize(tabListName, player, tags))
        }

        val tabListHeader = parseFormat(player, tabListConfig.header)
        val tabListFooter = parseFormat(player, tabListConfig.footer)

        val placeholders = TagResolver.resolver(
            MiniPlaceholdersIntegration.globalPlaceholders,
            audiencePlaceholders
        )

        player.sendPlayerListHeaderAndFooter(
            MiniMessage.miniMessage().deserialize(tabListHeader, player, placeholders),
            MiniMessage.miniMessage().deserialize(tabListFooter, player, placeholders),
        )
    }

    fun update() {
        Bukkit.getOnlinePlayers().forEach(TabList::send)
    }

    private fun parseFormat(player: Player, format: String): String {
        val format = PlaceholderAPIIntegration.parsePlaceholders(player, format)
        val config = BeeChat.instance.config

        if (!config.legacyFormatter.enable) {
            return format
        }

        return LegacyFormatTranslator.translate(config.legacyFormatter.character, format)
    }
}
