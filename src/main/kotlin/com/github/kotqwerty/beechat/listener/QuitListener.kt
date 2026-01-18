package com.github.kotqwerty.beechat.listener

import com.github.kotqwerty.beechat.configuration.Configuration
import com.github.kotqwerty.beechat.configuration.ChatConfiguration
import com.github.kotqwerty.beechat.extensions.spyModeEnabled
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class QuitListener(private val config: Configuration<ChatConfiguration>) : Listener {
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        if (config.access().spyMode.disableOnPlayerQuit) {
            val player = event.player
            if (player.spyModeEnabled) {
                player.spyModeEnabled = false
            }
        }
    }
}
