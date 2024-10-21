package fr.francoisdabonot.texttoolbareditordemo

import platform.UIKit.UIDevice

class IOSPlatform(
    override val system: CurrentOS = CurrentOS.IOS,
) : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()
