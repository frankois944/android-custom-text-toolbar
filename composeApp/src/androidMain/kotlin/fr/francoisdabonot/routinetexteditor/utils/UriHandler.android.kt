package fr.francoisdabonot.routinetexteditor.utils

import androidx.compose.ui.platform.UriHandler

actual fun uriHandler(
    handler: UriHandler?,
    string: String,
) {
    handler?.openUri(string)
}
