@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class,
)

package fr.francoisdabonot.routinetexteditor

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import fr.francoisdabonot.routinetexteditor.modal.HyperlinkAlertDialog
import fr.francoisdabonot.routinetexteditor.toolbar.CustomTextToolbar
import fr.francoisdabonot.routinetexteditor.utils.keyboardDisplayAsState
import kotlinx.coroutines.flow.collect
import kotlin.math.roundToInt

@Composable
fun RichEditorView(
    modifier: Modifier = Modifier,
    initialHtmlContent: String,
) {
    var linkAdded by remember { mutableStateOf(false) }
    var showAddLink by remember { mutableStateOf(false) }
    var displayContextualMenu by remember { mutableStateOf<Rect?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val richTextState = rememberRichTextState()
    val uriHandler = LocalUriHandler.current
    val isKeyboardOpen by keyboardDisplayAsState()
    var positionInRootEditor by remember { mutableStateOf(Offset.Zero) }
    val interactionSource = remember { MutableInteractionSource() }
    var currentInteraction by remember { mutableStateOf<Interaction?>(null) }

    val textToolBarConfig =
        CustomTextToolbar(
            onShowToolBarAtRect = { displayContextualMenu = it },
            onHideToolBarAtRect = { displayContextualMenu = null },
        )

    fun openLink(uri: String?) {
        uri?.let {
            displayContextualMenu = null
            uriHandler.openUri(it)
            keyboardController?.hide()
            focusManager.clearFocus()
            focusRequester.freeFocus()
            richTextState.selection = TextRange.Zero
        }
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            currentInteraction = interaction
        }
    }

    CompositionLocalProvider(LocalTextToolbar provides textToolBarConfig) {
        // Set initial content for the editor
        LaunchedEffect(Unit) { richTextState.setHtml(initialHtmlContent) }

        LaunchedEffect(richTextState.selection, currentInteraction) {
            if (richTextState.isLink && currentInteraction is PressInteraction.Release) {
                openLink(richTextState.selectedLinkUrl)
            }
        }

        Column(modifier = modifier) {
            EditorActionsRow(
                richTextState = richTextState,
                onAddLinkClick = { showAddLink = true },
                onOpenLinkClick = { openLink(richTextState.selectedLinkUrl) },
            )

            Box(modifier = Modifier.fillMaxSize()) {
                RichTextEditor(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .focusRequester(focusRequester)
                            .onGloballyPositioned { coordinates ->
                                positionInRootEditor = coordinates.positionInRoot()
                            },
                    interactionSource = interactionSource,
                    state = richTextState,
                )

                if (showAddLink) {
                    HyperlinkDialog(
                        richTextState = richTextState,
                        onDismiss = {
                            showAddLink = false
                            linkAdded = true
                        },
                        onConfirm = { label, url ->
                            handleLinkInsertion(richTextState, label, url, focusRequester)
                            showAddLink = false
                            linkAdded = true
                        },
                    )
                } else {
                    LaunchedEffect(isKeyboardOpen) {
                        if (!isKeyboardOpen) {
                            if (!linkAdded) {
                                focusManager.clearFocus()
                                focusRequester.freeFocus()
                            }
                            linkAdded = false
                        }
                    }
                }

                displayContextualMenu?.let { position ->
                    ContextualMenu(
                        position = position,
                        positionInRootEditor = positionInRootEditor,
                        richTextState = richTextState,
                        textToolBarConfig = textToolBarConfig,
                        onOpenLinkClick = { openLink(richTextState.selectedLinkUrl) },
                        onAddLinkClick = { showAddLink = true },
                    )
                }
            }
        }
    }
}

@Composable
fun EditorActionsRow(
    richTextState: RichTextState,
    onAddLinkClick: () -> Unit,
    onOpenLinkClick: () -> Unit,
) {
    Row {
        if (richTextState.isLink) {
            IconButton(onClick = onOpenLinkClick) {
                Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = "Open link")
            }
        }
        IconButton(onClick = onAddLinkClick) {
            Icon(
                imageVector = if (richTextState.selectedLinkUrl.isNullOrEmpty()) Icons.Default.AddLink else Icons.Default.Link,
                contentDescription = if (richTextState.selectedLinkUrl.isNullOrEmpty()) "Add link" else "Edit link",
            )
        }
    }
}

@Composable
fun HyperlinkDialog(
    richTextState: RichTextState,
    onDismiss: () -> Unit,
    onConfirm: (String?, String) -> Unit,
) {
    val currentLabel =
        richTextState.selectedLinkText ?: richTextState
            .toText()
            .substring(richTextState.selection)
            .takeIf { richTextState.selection.length > 0 }

    HyperlinkAlertDialog(
        hasLabel = richTextState.selection.length > 0,
        currentLabel = currentLabel,
        currentURL = richTextState.selectedLinkUrl,
        onDismissRequest = onDismiss,
        onConfirmation = onConfirm,
    )
}

@Composable
fun ContextualMenu(
    position: Rect,
    positionInRootEditor: Offset,
    richTextState: RichTextState,
    textToolBarConfig: CustomTextToolbar,
    onOpenLinkClick: () -> Unit,
    onAddLinkClick: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .height(44.dp)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(
                            position.topLeft.x.roundToInt(),
                            (position.topLeft.y - positionInRootEditor.y + 44).roundToInt(),
                        )
                    }
                },
        shape = RoundedCornerShape(50),
    ) {
        Row {
            if (richTextState.isLink) {
                IconButton(onClick = onOpenLinkClick) {
                    Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = "Open link")
                }
            }
            IconButton(onClick = onAddLinkClick) {
                Icon(
                    imageVector = if (richTextState.selectedLinkUrl.isNullOrEmpty()) Icons.Default.AddLink else Icons.Default.Link,
                    contentDescription = if (richTextState.selectedLinkUrl.isNullOrEmpty()) "Add link" else "Edit link",
                )
            }
            if (richTextState.selection.length > 0) {
                IconButton(onClick = { textToolBarConfig.copyRequested() }) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy")
                }
                IconButton(onClick = { textToolBarConfig.cutRequested() }) {
                    Icon(imageVector = Icons.Default.ContentCut, contentDescription = "Cut")
                }
            } else {
                IconButton(onClick = { textToolBarConfig.pasteRequested() }) {
                    Icon(imageVector = Icons.Default.ContentPaste, contentDescription = "Paste")
                }
            }
            IconButton(onClick = { textToolBarConfig.selectAllRequested() }) {
                Icon(imageVector = Icons.Default.SelectAll, contentDescription = "Select all")
            }
        }
    }
}

fun handleLinkInsertion(
    richTextState: RichTextState,
    label: String?,
    url: String,
    focusRequester: FocusRequester,
) {
    when {
        label != null && richTextState.selectedLinkText == null && richTextState.selectedLinkUrl == null -> {
            richTextState.addLink(text = label, url = url)
        }
        richTextState.selection.length > 0 && richTextState.selectedLinkUrl == null -> {
            richTextState.addLinkToSelection(url = url)
        }
        richTextState.selectedLinkText != null && richTextState.selectedLinkUrl != null -> {
            richTextState.updateLink(url = url)
        }
    }
    focusRequester.requestFocus()
}
