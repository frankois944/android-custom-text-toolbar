package fr.francoisdabonot.routinetexteditor.toolbar

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus

// https://github.com/androidx/androidx/blob/androidx-main/compose/ui/ui/src/androidMain/kotlin/androidx/compose/ui/platform/AndroidTextToolbar.android.kt

class CustomTextToolbar(
    private val onShowToolBarAtRect: (Rect) -> Unit,
    private val onHideToolBarAtRect: () -> Unit,
) : TextToolbar {
    private var actionMode: String? = null
    override var status: TextToolbarStatus = TextToolbarStatus.Hidden
        private set

    private var onCopyRequested: (() -> Unit)? = null
    private var onPasteRequested: (() -> Unit)? = null
    private var onCutRequested: (() -> Unit)? = null
    private var onSelectAllRequested: (() -> Unit)? = null

    fun copyRequested() {
        this.onCopyRequested?.invoke()
        this.hide()
    }

    fun pasteRequested() {
        this.onPasteRequested?.invoke()
        this.hide()
    }

    fun cutRequested() {
        this.onCutRequested?.invoke()
        this.hide()
    }

    fun selectAllRequested() {
        this.onSelectAllRequested?.invoke()
        this.hide()
    }

    override fun hide() {
        status = TextToolbarStatus.Hidden
        onHideToolBarAtRect()
        actionMode = null
    }

    override fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)?,
        onPasteRequested: (() -> Unit)?,
        onCutRequested: (() -> Unit)?,
        onSelectAllRequested: (() -> Unit)?,
    ) {
        if (actionMode == null) {
            status = TextToolbarStatus.Shown
            onShowToolBarAtRect(rect)
        } else {
            onHideToolBarAtRect()
        }

        this.onCopyRequested = onCopyRequested
        this.onCutRequested = onCutRequested
        this.onPasteRequested = onPasteRequested
        this.onCopyRequested = onCopyRequested
        this.onSelectAllRequested = onSelectAllRequested
    }
}
