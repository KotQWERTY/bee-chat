package com.github.kotqwerty.beechat

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

class Task(val plugin: Plugin, val execute: () -> Unit) {
    private var bukkitTask: BukkitTask? = null

    fun runTimer(period: Long, delay: Long = 0) {
        stop()
        bukkitTask = Bukkit.getScheduler().runTaskTimer(
            plugin,
            execute,
            delay,
            period
        )
    }

    fun stop() {
        bukkitTask?.cancel()
    }
}
