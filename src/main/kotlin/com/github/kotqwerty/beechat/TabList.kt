package com.github.kotqwerty.beechat

import com.github.kotqwerty.beechat.integration.MiniPlaceholdersIntegration
import com.github.kotqwerty.beechat.integration.PlaceholderAPIIntegration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object TabList {
    fun send(player: Player) {
        val tabListConfig = BeeChat.instance.config.tabList
        val audiencePlaceholders = MiniPlaceholdersIntegration.audiencePlaceholders

        if (tabListConfig.playerName.isNotEmpty()) {
            val tabListName = PlaceholderAPIIntegration.parsePlaceholders(player, tabListConfig.playerName)

            val tags = TagResolver.resolver(
                Placeholders.name(player.displayName()),
                audiencePlaceholders
            )

            player.playerListName(MiniMessage.miniMessage().deserialize(tabListName, player, tags))
        }

        var tabListHeader = tabListConfig.header
        var tabListFooter = tabListConfig.footer

        tabListHeader = PlaceholderAPIIntegration.parsePlaceholders(player, tabListHeader)
        tabListFooter = PlaceholderAPIIntegration.parsePlaceholders(player, tabListFooter)

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
}
