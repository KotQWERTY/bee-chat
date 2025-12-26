package com.github.kotqwerty.beechat.configuration

class Configuration<T>(private val supplier: () -> T) {
    @Volatile
    private var value = supplier()

    fun access() = value

    fun reload() {
        value = supplier()
    }
}
