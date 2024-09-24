package fr.francoisdabonot.routinetexteditor

import android.os.Build

class AndroidPlatform(
    override val system: CurrentOS = CurrentOS.Android,
) : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
