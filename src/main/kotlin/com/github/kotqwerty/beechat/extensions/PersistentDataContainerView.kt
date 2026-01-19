package com.github.kotqwerty.beechat.extensions

import io.papermc.paper.persistence.PersistentDataContainerView
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType

fun PersistentDataContainerView.getStringOrNull(key: NamespacedKey): String? =
    get(key, PersistentDataType.STRING)

fun PersistentDataContainerView.getBoolOrNull(key: NamespacedKey): Boolean? =
    get(key, PersistentDataType.BOOLEAN)

fun PersistentDataContainerView.getBool(key: NamespacedKey, default: Boolean = false) =
    getBoolOrNull(key) ?: default
