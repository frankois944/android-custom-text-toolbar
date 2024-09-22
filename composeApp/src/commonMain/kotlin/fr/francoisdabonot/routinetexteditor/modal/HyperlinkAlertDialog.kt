package fr.francoisdabonot.routinetexteditor.modal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun HyperlinkAlertDialog(
    hasLabel: Boolean,
    currentLabel: String?,
    currentURL: String? = null,
    onDismissRequest: () -> Unit,
    onConfirmation: (String?, String) -> Unit,
) {
    val (label, setLabel) = remember { mutableStateOf(TextFieldValue("")) }
    val (uri, setUri) = remember { mutableStateOf(TextFieldValue(currentURL ?: "https://")) }
    val labelFocusRequester = remember { FocusRequester() }
    val uriFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (!hasLabel) {
            labelFocusRequester.requestFocus()
        } else {
            uriFocusRequester.requestFocus()
        }
    }

    fun confirmInputs() {
        if ((hasLabel || label.text.isNotEmpty()) && uri.text.isNotEmpty()) {
            onConfirmation(label.text.takeIf { label.text.isNotEmpty() }, uri.text)
        }
    }

    AlertDialog(
        icon = {
            Icon(
                imageVector = if (!hasLabel) Icons.Default.AddLink else Icons.Default.Link,
                contentDescription = null,
            )
        },
        title = {
            Text(text = currentLabel ?: "New Link")
        },
        text = {
            Column {
                if (!hasLabel) {
                    TextField(
                        modifier = Modifier.focusRequester(labelFocusRequester),
                        value = label.copy(selection = TextRange(label.text.length)),
                        onValueChange = setLabel,
                        label = { Text("Name of the link") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions =
                            KeyboardActions(
                                onNext = {
                                    uriFocusRequester.requestFocus()
                                },
                            ),
                    )
                }
                TextField(
                    modifier = Modifier.focusRequester(uriFocusRequester),
                    value = uri.copy(selection = TextRange(uri.text.length)),
                    onValueChange = setUri,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Uri),
                    label = { Text("URL of the website") },
                    keyboardActions =
                        KeyboardActions(
                            onDone = {
                                confirmInputs()
                            },
                        ),
                )
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                enabled = (hasLabel || label.text.isNotEmpty()) && uri.text.isNotEmpty(),
                onClick = {
                    confirmInputs()
                },
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                },
            ) {
                Text("Dismiss")
            }
        },
    )
}
