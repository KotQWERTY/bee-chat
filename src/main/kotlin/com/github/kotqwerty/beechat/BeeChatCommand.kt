package com.github.kotqwerty.beechat

import com.github.kotqwerty.beechat.extensions.getBool
import com.github.kotqwerty.beechat.extensions.setBool
import com.mojang.brigadier.Command
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player

class BeeChatCommand(private val plugin: BeeChat) {
    fun root(): LiteralCommandNode<CommandSourceStack> = Commands.literal("beechat")
        .then(reload())
        .then(spy())
        .build()

    private fun reload() = Commands.literal("reload")
        .requires { it.sender.hasPermission(Permissions.reload) }
        .executes { ctx ->
            val sender = ctx.source.sender

            plugin.reload()
            sender.sendRichMessage(plugin.messages.access().reload)

            Command.SINGLE_SUCCESS
        }

    private fun spy() = Commands.literal("spy")
        .requires { it.sender.hasPermission(Permissions.spy) }
        .executes { ctx ->
            val sender = ctx.source.sender

            val messages = plugin.messages.access()

            if (sender !is Player) {
                sender.sendRichMessage(messages.notPlayer)
                return@executes Command.SINGLE_SUCCESS
            }

            if (sender.persistentDataContainer.getBool(Keys.SPY_MODE)) {
                sender.persistentDataContainer.setBool(Keys.SPY_MODE, false)
                sender.sendRichMessage(messages.spyModeDisabled)
            } else {
                sender.persistentDataContainer.setBool(Keys.SPY_MODE, true)
                sender.sendRichMessage(messages.spyModeEnabled)
            }

            Command.SINGLE_SUCCESS
        }
}
