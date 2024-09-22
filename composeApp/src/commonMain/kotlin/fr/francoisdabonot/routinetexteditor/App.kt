package fr.francoisdabonot.routinetexteditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column {
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    text =
                        """
                        Technical Test by François Dabonot for Routine
                        It has a customized contextual menu when selecting text in the editor
                        you can :
                            - open/create/edit a link from the toolbar button or contextual menu
                        """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium,
                )
                RichEditorView(
                    Modifier.fillMaxSize().padding(16.dp).background(Color.LightGray),
                    initialHtmlContent =
                        """
                        <div>
                           Routine Demo editor
                        </div>
                        <div>
                            <a href="https://routine.co">Routine website</a>
                        </div>
                        """.trimIndent(),
                )
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    App()
}
