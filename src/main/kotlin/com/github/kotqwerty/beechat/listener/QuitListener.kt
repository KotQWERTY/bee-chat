package com.github.kotqwerty.beechat.listener

import com.github.kotqwerty.beechat.configuration.Configuration
import com.github.kotqwerty.beechat.config_v1.PluginConfig
import com.github.kotqwerty.beechat.extensions.spyModeEnabled
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class QuitListener(private val config: Configuration<PluginConfig>) : Listener {
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val chatConfig = config.access().chat

        if (chatConfig.spy.disableOnLeave) {
            val player = event.player
            if (player.spyModeEnabled) {
                player.spyModeEnabled = false
            }
        }
    }
}
