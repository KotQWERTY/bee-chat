package com.github.kotqwerty.beechat.extensions

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

fun PersistentDataContainer.setBool(key: NamespacedKey, value: Boolean) {
    set(key, PersistentDataType.BOOLEAN, value)
}
