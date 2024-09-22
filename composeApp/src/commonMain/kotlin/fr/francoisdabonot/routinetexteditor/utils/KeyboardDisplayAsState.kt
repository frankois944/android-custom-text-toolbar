package fr.francoisdabonot.routinetexteditor.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
expect fun keyboardDisplayAsState(): State<Boolean>
