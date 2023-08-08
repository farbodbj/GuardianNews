package com.bale_bootcamp.guardiannews.ui.settings.model


enum class OrderBy(val value: String) {
    NEWEST("newest"),
    OLDEST("oldest"),
    RELEVANCE("relevance");
    override fun toString(): String = this.value
    companion object {
        fun findByStr(value: String): OrderBy {
            return OrderBy.values().find {
                it.value == value
            } ?: throw IllegalArgumentException("value $value not found in enum")
        }
    }
}

enum class ColorTheme(val value: String) {
    WHITE("white"),
    SKY_BLUE("sky_blue"),
    DARK_BLUE("dark_blue"),
    LIGHT_GREEN("light_green"),
    GREEN("green");
    override fun toString(): String = this.value
    companion object {
        fun findByStr(value: String): ColorTheme {
            return ColorTheme.values().find {
                it.value == value
            } ?: throw IllegalArgumentException("value $value not found in enum")
        }
    }
}

enum class TextSize(val value: String) {
    SMALL("small"),
    MEDIUM("medium"),
    LARGE("large");
    override fun toString(): String = this.value
    companion object {
        fun findByStr(value: String): TextSize {
            return TextSize.values().find {
                it.value == value
            } ?: throw IllegalArgumentException("value $value not found in enum")
        }
    }
}