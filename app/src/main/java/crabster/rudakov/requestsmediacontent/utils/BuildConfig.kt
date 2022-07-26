package crabster.rudakov.requestsmediacontent.utils

import java.lang.Boolean
import kotlin.arrayOf

object BuildConfig {
    val DEBUG = Boolean.parseBoolean("true")
    const val APPLICATION_ID = "ru.rusatom.utilities.water.requests.dw"
    const val BUILD_TYPE = "debug"
    const val FLAVOR = "dw"
    const val VERSION_CODE = 9
    const val VERSION_NAME = "1.2.7-dw"

    // Fields from product flavor: dw
    const val SYSTEM_TYPE = "WATER"

    // Fields from default config.
    const val BLOCK_AGE = true
    const val BLOCK_GEOTAG = true
    const val BLOCK_SCREENSHOT = true
    const val DB = "water.db"
    const val HASH = "HZ1WxHh9tV0KiJrAXT9Y"
    const val HOST = "http://iprv027.info-pro.ru:5000"
    const val MAPKIT_KEY = "9040d619-a17c-4158-aa36-b7df4c16b7d8"
    const val MAX_AGE_MINUTES = 180
    const val MAX_PHOTO_DISTANCE = 300
    const val OAUTH_APP_ID = "8rRYkPqv4rMm9XLnK9Mt"
    const val OAUTH_SECRET = "E2DKtaxAWBpufa6xZgC8bhnwjBY6URXe4vkjHQhXQExdMLzw"
    val TECH_SUPPORT_EMAIL = arrayOf(
        "EYuMokin@rusatom-utilities.ru",
        "SBoKashutin@rusatom-utilities.ru",
        "YAMilyuta@rusatom-utilities.ru"
    )
}