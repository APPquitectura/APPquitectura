package com.etsisi.appquitectura.data.model.enums

enum class ScoreLevel(val value: String, val number: Int) {
    LEVEL_0("level0", 0),
    LEVEL_1("level1", 1),
    LEVEL_2("level2",2),
    LEVEL_3("level3", 3),
    LEVEL_4("level4", 4),
    LEVEL_5("level5", 5),
    LEVEL_6("level6", 6),
    LEVEL_7("level7", 7),
    LEVEL_8("level8", 8),
    LEVEL_9("level9", 9),
    LEVEL_10("level10", 10);

    companion object {
        fun parseToLevel(s: String): ScoreLevel {
           return values().find { it.value.equals(s, true) } ?: LEVEL_0
        }
    }
}