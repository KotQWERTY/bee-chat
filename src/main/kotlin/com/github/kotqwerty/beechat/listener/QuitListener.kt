package com.github.kotqwerty.beechat.listener

import com.github.kotqwerty.beechat.BeeChat
import com.github.kotqwerty.beechat.extensions.spyModeEnabled
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object QuitListener : Listener {
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val config = BeeChat.instance.config.access().chat
        if (config.spy.disableOnLeave) {
            val player = event.player
            if (player.spyModeEnabled) {
                player.spyModeEnabled = false
            }
        }
    }
}
