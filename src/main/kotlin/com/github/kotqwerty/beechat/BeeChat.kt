package com.github.kotqwerty.beechat

import com.github.kotqwerty.beechat.configuration.PluginConfig
import com.github.kotqwerty.beechat.configuration.Configuration
import com.github.kotqwerty.beechat.extensions.register
import com.github.kotqwerty.beechat.listener.ChatListener
import com.github.kotqwerty.beechat.listener.JoinListener
import com.github.kotqwerty.beechat.listener.QuitListener
import com.github.kotqwerty.beechat.utils.UpdateChecker
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.yaml.YamlConfigurationLoader

class BeeChat : JavaPlugin() {
    private val configurations = mutableMapOf<String, Configuration<*>>()

    val config = addConfiguration("config.yml", default = ::PluginConfig)

    private val tabListUpdateTask = Task(execute = TabList::update)

    init {
        instance = this
    }

    private inline fun <reified T> addConfiguration(
        name: String,
        noinline default: () -> T,
    ): Configuration<T> {
        val configFile = dataFolder.resolve(name)

        val loader = YamlConfigurationLoader.builder()
            .file(configFile)
            .defaultOptions { options ->
                options.serializers { builder ->
                    builder.registerAnnotatedObjects(objectMapperFactory())
                }
            }
            .build()

        val configuration = Configuration {
            if (!configFile.exists()) {
                saveResource(name, false)
            }

            try {
                loader.load().get<T>(default)
            } catch (e: Exception) {
                componentLogger.error("Failed to load configuration '$name':", e)
                componentLogger.warn("Using the default configuration for '$name' due to a previous error")
                default()
            }
        }

        configurations[name] = configuration

        return configuration
    }

    override fun onEnable() {
        reload()

        Permissions.register()

        registerCommand()

        ChatListener.register(this)
        JoinListener.register(this)
        QuitListener.register(this)

        Metrics(this, 24314)

        checkForUpdatesAsync()
    }

    fun reload() {
        for ((configName, config) in configurations) {
            componentLogger.info("Reloading configuration '$configName'...")
            config.reload()
        }

        if (shouldRestartTabListTask()) {
            tabListUpdateTask.runTimer(period = config.access().tabList.updatePeriod)
        }
    }

    private fun shouldRestartTabListTask(): Boolean = config.access().run {
        tabList.enable && tabList.updatePeriod > 0
    }

    private fun checkForUpdatesAsync() {
        if (config.access().checkForUpdates) {
            Task { UpdateChecker.checkForUpdates(componentLogger) }.runAsync()
        }
    }

    private fun registerCommand() {
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS.newHandler { event ->
            event.registrar().register(BeeChatCommand.root())
        })
    }

    companion object {
        lateinit var instance: BeeChat private set
    }
}
