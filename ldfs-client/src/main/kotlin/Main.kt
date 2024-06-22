import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    val tabOrder = listOf(TabType.LOCAL, TabType.LDFS)
    var selectedTabIndex by remember { mutableStateOf(0) }

    MaterialTheme {
        Column {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabOrder.forEachIndexed { index, tabType ->
                    Tab(
                        text = { Text(tabType.name) },
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                        },
                    )
                }
            }

            when (tabOrder[selectedTabIndex]) {
                TabType.LOCAL -> {
                    LocalFileExplorerScreen()
                }
                TabType.LDFS -> {
                    LdfsFileExplorerScreen()
                }
            }
        }
    }
}

fun main() =
    application {
        Window(onCloseRequest = ::exitApplication) {
            App()
        }
    }
