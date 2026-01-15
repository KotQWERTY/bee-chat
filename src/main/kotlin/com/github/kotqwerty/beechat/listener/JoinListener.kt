package com.github.kotqwerty.beechat.listener

import com.github.kotqwerty.beechat.PlayerList
import com.github.kotqwerty.beechat.configuration.Configuration
import com.github.kotqwerty.beechat.configuration.PluginConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener(
    private val pluginConfig: Configuration<PluginConfiguration>,
    private val playerList: PlayerList,
) : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (pluginConfig.access().enablePlayerList) {
            playerList.send(event.player)
        }
    }
}
