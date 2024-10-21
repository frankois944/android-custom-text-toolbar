package fr.francoisdabonot.texttoolbareditordemo.utils

import androidx.compose.ui.platform.UriHandler
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun uriHandler(
    handler: UriHandler?,
    string: String,
) {
    NSURL.URLWithString(string)?.let {
        if (UIApplication.sharedApplication.canOpenURL(it)) {
            UIApplication.sharedApplication.openURL(it, options = emptyMap<Any?, Any>(), completionHandler = {
            })
        }
    }
}
