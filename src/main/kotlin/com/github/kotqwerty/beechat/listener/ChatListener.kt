package com.github.kotqwerty.beechat.listener

import com.github.kotqwerty.beechat.Keys
import com.github.kotqwerty.beechat.Placeholders
import com.github.kotqwerty.beechat.chat.ChatChannel
import com.github.kotqwerty.beechat.configuration.Configuration
import com.github.kotqwerty.beechat.configuration.ChatConfiguration
import com.github.kotqwerty.beechat.extensions.getStringOrNull
import com.github.kotqwerty.beechat.extensions.spyModeEnabled
import com.github.kotqwerty.beechat.integration.MiniPlaceholdersIntegration
import com.github.kotqwerty.beechat.integration.PlaceholderAPIIntegration
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ChatListener(private val config: Configuration<ChatConfiguration>) : Listener {
    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val sender = event.player
        var message = event.signedMessage().message()

        val channel = findChannel(sender, message)

        filterViewers(sender, channel, event.viewers())

        if (channel.messagePrefix.isNotEmpty()) {
            message = message.removePrefix(channel.messagePrefix)
        }

        val config = config.access()
        val format = PlaceholderAPIIntegration.parsePlaceholders(sender, channel.format)

        event.renderer { source, sourceDisplayName, _, viewer ->
            val baseTags = TagResolver.resolver(
                Placeholders.name(sourceDisplayName),
                Placeholders.message(source, message),
                MiniPlaceholdersIntegration.audiencePlaceholders,
            )

            val formattedMessage = MiniMessage.miniMessage().deserialize(format, source, baseTags)

            val channelTags = TagResolver.resolver(baseTags, Placeholders.formattedMessage(formattedMessage))
            val channelMessage = MiniMessage.miniMessage().deserialize(channel.format, source, channelTags)

            val shouldApplySpyFormatting = viewer is Player && !canReceiveMessage(sender, viewer, channel)
            if (!shouldApplySpyFormatting) return@renderer channelMessage

            val spyTags = TagResolver.resolver(channelTags, Placeholders.channelMessage(channelMessage))
            MiniMessage.miniMessage().deserialize(config.spyMode.format, source, spyTags)
        }
    }

    private fun findChannel(sender: Player, message: String): ChatChannel {
        val config = config.access()
        val channels = config.channels

        for ((_, channel) in channels) {
            if (channel.messagePrefix.isNotEmpty() && message.startsWith(channel.messagePrefix)) {
                return channel
            }
        }

        val persistentChannelName = sender.persistentDataContainer.getStringOrNull(Keys.CHAT_CHANNEL)

        val persistentChannel = config.channels[persistentChannelName]?.takeIf { channel ->
            channel.sendPermission.isEmpty() || sender.hasPermission(channel.sendPermission)
        }

        return persistentChannel ?: channels.getValue(config.defaultChannel)
    }

    private fun filterViewers(sender: Player, channel: ChatChannel, viewers: MutableSet<Audience>) {
        viewers.removeAll { viewer ->
            if (viewer !is Player) return@removeAll false

            !canReceiveMessage(sender, viewer, channel) && !viewer.spyModeEnabled
        }
    }

    private fun canReceiveMessage(sender: Player, recipient: Audience, channel: ChatChannel): Boolean {
        if (recipient !is Player) return true

        val hasPermission = channel.receivePermission.isEmpty() || recipient.hasPermission(channel.receivePermission)
        val isAccessible = channel.maxDistance <= 0 || recipient.location.distance(sender.location) <= channel.maxDistance

        return hasPermission && isAccessible
    }
}
