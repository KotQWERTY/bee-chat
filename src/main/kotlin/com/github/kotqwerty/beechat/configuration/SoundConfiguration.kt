package com.github.kotqwerty.beechat.configuration

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class SoundConfiguration(
    val key: String,
    val source: Sound.Source = Sound.Source.UI,
    val pitch: Float = 1f,
) {
    fun buildSound() = Sound.sound {
        it.type(Key.key(key))
        it.source(source)
        it.pitch(pitch)
    }
}
