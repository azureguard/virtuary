package com.virtuary.app.util

import android.annotation.TargetApi
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import java.util.*

fun ContextWrapper.wrap(language: String, theme: Int): ContextWrapper {
    val config = baseContext.resources.configuration
    val sysLocale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.getSystemLocale()
    } else {
        this.getSystemLocaleLegacy()
    }

    if (language.isNotEmpty() && sysLocale.language != language) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.setSystemLocale(locale)
        } else {
            this.setSystemLocaleLegacy(locale)
        }
    }

    when (theme) {
        AppCompatDelegate.MODE_NIGHT_NO -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            config.uiMode = Configuration.UI_MODE_NIGHT_NO
        }
        AppCompatDelegate.MODE_NIGHT_YES -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            config.uiMode = Configuration.UI_MODE_NIGHT_YES
        }
    }

    val context = baseContext.createConfigurationContext(config)
    return ContextWrapper(context)
}

@Suppress("DEPRECATION")
fun ContextWrapper.getSystemLocaleLegacy(): Locale {
    val config = baseContext.resources.configuration
    return config.locale
}

@TargetApi(Build.VERSION_CODES.N)
fun ContextWrapper.getSystemLocale(): Locale {
    val config = baseContext.resources.configuration
    return config.locales[0]
}

@Suppress("DEPRECATION")
fun ContextWrapper.setSystemLocaleLegacy(locale: Locale) {
    val config = baseContext.resources.configuration
    config.locale = locale
}

@TargetApi(Build.VERSION_CODES.N)
fun ContextWrapper.setSystemLocale(locale: Locale) {
    val config = baseContext.resources.configuration
    config.setLocale(locale)
}
