package com.github.kotqwerty.beechat

import com.github.kotqwerty.beechat.config_v1.PluginConfig
import com.github.kotqwerty.beechat.configuration.ChatConfiguration
import com.github.kotqwerty.beechat.configuration.Configuration
import com.github.kotqwerty.beechat.configuration.Messages
import com.github.kotqwerty.beechat.configuration.PlayerListConfiguration
import com.github.kotqwerty.beechat.configuration.PluginConfiguration
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

    val config = addConfiguration("config_v1.yml", default = ::PluginConfig)

    val pluginConfig = addConfiguration("config.yml", default = ::PluginConfiguration)
    val messages = addConfiguration("messages.yml", default = ::Messages)
    val chatConfig = addConfiguration("chat.yml", default = ::ChatConfiguration)
    val playerListConfig = addConfiguration("player-list.yml", default = ::PlayerListConfiguration)

    private val playerList = PlayerList(playerListConfig)
    private val playerListUpdateTask = Task(plugin = this, execute = playerList::update)

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

        ChatListener(config).register(this)
        JoinListener(pluginConfig, playerList).register(this)
        QuitListener(chatConfig).register(this)

        Metrics(this, 24314)

        if (pluginConfig.access().checkForUpdates) {
            checkForUpdates()
        }
    }

    fun reload() {
        for ((configName, config) in configurations) {
            componentLogger.info("Loading configuration '$configName'...")
            config.reload()
        }

        if (shouldRestartTabListTask()) {
            playerListUpdateTask.runTimer(period = playerListConfig.access().updatePeriod)
        }
    }

    private fun shouldRestartTabListTask(): Boolean {
        val isEnabled = pluginConfig.access().enablePlayerList
        val updatePeriod = playerListConfig.access().updatePeriod

        return isEnabled && updatePeriod > 0
    }

    private fun checkForUpdates() {
        server.asyncScheduler.runNow(this) {
            UpdateChecker(pluginMeta).checkForUpdates(componentLogger)
        }
    }

    private fun registerCommand() {
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS.newHandler { event ->
            val command = BeeChatCommand(plugin = this)
            event.registrar().register(command.root())
        })
    }

    companion object {
        const val NAMESPACE = "beechat"
    }
}
