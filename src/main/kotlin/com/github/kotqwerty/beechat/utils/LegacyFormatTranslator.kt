package com.github.kotqwerty.beechat.utils

import kotlin.text.iterator

object LegacyFormatTranslator {
    val LEGACY_CODE_TO_MINI_MESSAGE = mapOf(
        '4' to "<dark_red>",
        'c' to "<red>",
        '6' to "<gold>",
        'e' to "<yellow>",
        '2' to "<dark_green>",
        'a' to "<green>",
        'b' to "<aqua>",
        '3' to "<dark_aqua>",
        '1' to "<dark_blue>",
        '9' to "<blue>",
        'd' to "<light_purple>",
        '5' to "<dark_purple>",
        'f' to "<white>",
        '7' to "<gray>",
        '8' to "<dark_gray>",
        '0' to "<black>",
        'l' to "<b>",
        'n' to "<u>",
        'o' to "<i>",
        'k' to "<obf>",
        'm' to "<st>",
        'r' to "<reset>",
    )

    fun translate(legacyChar: Char, string: String): String {
        val builder = StringBuilder(string.length)
        var legacyMode = false

        for (char in string) {
            if (legacyMode) {
                legacyMode = false

                if (char == legacyChar) {
                    builder.append(legacyChar)
                    legacyMode = true
                    continue
                }

                val miniMessageTag = LEGACY_CODE_TO_MINI_MESSAGE[char]

                if (miniMessageTag != null) {
                    builder.append(miniMessageTag)
                    continue
                }

                builder.append(legacyChar)
                builder.append(char)
                continue
            }

            if (char == legacyChar) {
                legacyMode = true
                continue
            }

            builder.append(char)
        }

        if (legacyMode) {
            builder.append(legacyChar)
        }

        return builder.toString()
    }
}
