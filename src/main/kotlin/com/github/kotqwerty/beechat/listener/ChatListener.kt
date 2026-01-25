package com.github.kotqwerty.beechat.listener

import com.github.kotqwerty.beechat.BeeChat
import com.github.kotqwerty.beechat.Placeholders
import com.github.kotqwerty.beechat.configuration.ChatChannelConfig
import com.github.kotqwerty.beechat.extensions.spyModeEnabled
import com.github.kotqwerty.beechat.integration.MiniPlaceholdersIntegration
import com.github.kotqwerty.beechat.integration.PlaceholderAPIIntegration
import com.github.kotqwerty.beechat.utils.LegacyFormatTranslator
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object ChatListener : Listener {
    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val config = BeeChat.instance.config.chat
        val sender = event.player
        val format = parseFormat(sender, config.messageFormat)
        if (format.isEmpty()) return

        var message = event.signedMessage().message()
        val channel = findChannel(sender, message, config.channels)

        channel?.let {
            message = message.removePrefix(it.identifier)
            filterViewers(sender, it, event.viewers())
        }

        event.renderer { source, sourceDisplayName, _, viewer ->
            val baseTags = TagResolver.resolver(
                Placeholders.name(sourceDisplayName),
                Placeholders.message(source, message),
                MiniPlaceholdersIntegration.audiencePlaceholders,
            )

            val formattedMessage = MiniMessage.miniMessage().deserialize(format, source, baseTags)
            if (channel == null) return@renderer formattedMessage

            val channelTags = TagResolver.resolver(baseTags, Placeholders.formattedMessage(formattedMessage))
            val channelMessage = MiniMessage.miniMessage().deserialize(parseFormat(source, channel.format), source, channelTags)

            val shouldApplySpyFormatting = viewer is Player && !channel.canSee(sender, viewer)
            if (!shouldApplySpyFormatting) return@renderer channelMessage

            val spyTags = TagResolver.resolver(channelTags, Placeholders.channelMessage(channelMessage))
            MiniMessage.miniMessage().deserialize(parseFormat(source, config.spy.format), source, spyTags)
        }
    }

    private fun parseFormat(player: Player, format: String): String {
        val format = PlaceholderAPIIntegration.parsePlaceholders(player, format)
        val config = BeeChat.instance.config

        if (!config.legacyFormatter.enable) {
            return format
        }

        return LegacyFormatTranslator.translate(config.legacyFormatter.character, format)
    }

    private fun findChannel(sender: Player, message: String, channels: List<ChatChannelConfig>) =
        channels.find { channel ->
            (channel.permission.isEmpty() || sender.hasPermission(channel.permission)) &&
                    message.startsWith(channel.identifier)
        }

    private fun filterViewers(sender: Player, channel: ChatChannelConfig, viewers: MutableSet<Audience>) {
        viewers.removeAll { viewer ->
            viewer is Player && !channel.canSee(sender, viewer) && !viewer.spyModeEnabled
        }
    }
}
