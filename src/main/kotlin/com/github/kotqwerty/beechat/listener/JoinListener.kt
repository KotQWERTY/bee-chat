package com.github.kotqwerty.beechat.listener

import com.github.kotqwerty.beechat.TabList
import com.github.kotqwerty.beechat.configuration.Configuration
import com.github.kotqwerty.beechat.configuration.PluginConfig
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener(private val config: Configuration<PluginConfig>) : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (config.access().tabList.enable) {
            TabList.send(event.player)
        }
    }
}
