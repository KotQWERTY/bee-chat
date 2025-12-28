package com.github.kotqwerty.beechat

import com.github.kotqwerty.beechat.extensions.spyModeEnabled
import com.mojang.brigadier.Command
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player

object BeeChatCommand {
    fun root(): LiteralCommandNode<CommandSourceStack> = Commands.literal("beechat")
        .then(reload())
        .then(spy())
        .build()

    private fun reload() = Commands.literal("reload")
        .requires { it.sender.hasPermission(Permissions.reload) }
        .executes { ctx ->
            val sender = ctx.source.sender

            val plugin = BeeChat.instance
            plugin.reload()

            sender.sendRichMessage(plugin.config.access().messages.reload)

            Command.SINGLE_SUCCESS
        }

    private fun spy() = Commands.literal("spy")
        .requires { it.sender.hasPermission(Permissions.spy) }
        .executes { ctx ->
            val sender = ctx.source.sender

            val plugin = BeeChat.instance
            val messages = plugin.config.access().messages

            if (sender !is Player) {
                sender.sendRichMessage(messages.notPlayer)
                return@executes Command.SINGLE_SUCCESS
            }

            if (sender.spyModeEnabled) {
                sender.spyModeEnabled = false
                sender.sendRichMessage(messages.spyModeDisabled)
            } else {
                sender.spyModeEnabled = true
                sender.sendRichMessage(messages.spyModeEnabled)
            }

            Command.SINGLE_SUCCESS
        }
}
