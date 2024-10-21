package fr.francoisdabonot.texttoolbareditordemo.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
actual fun keyboardDisplayAsState(): State<Boolean> {
    val keyboardState = remember { mutableStateOf(false) }
    return keyboardState
}
