package com.github.kotqwerty.beechat.extensions

import com.github.kotqwerty.beechat.Keys
import com.github.kotqwerty.beechat.Permissions
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

var Player.spyModeEnabled: Boolean
    get() = hasPermission(Permissions.spy) && persistentDataContainer
        .getOrDefault(Keys.SPY_MODE, PersistentDataType.BOOLEAN, false)
    set(value) = persistentDataContainer
        .set(Keys.SPY_MODE, PersistentDataType.BOOLEAN, value)
