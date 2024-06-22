import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.EventQueue
import java.awt.FileDialog
import java.awt.Frame
import java.util.UUID

@Composable
fun FileSelectorDialog(
    isDialogOpen: Boolean,
    onFileChosen: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (isDialogOpen) {
        Dialog(onDismissRequest = onDismissRequest) {
            LaunchedEffect(Unit) {
                var directory: String? = null
                var file: String? = null
                withContext(Dispatchers.IO) {
                    EventQueue.invokeAndWait {
                        val fileDialog = FileDialog(Frame())
                        fileDialog.isVisible = true
                        directory = fileDialog.directory
                        file = fileDialog.file
                    }
                }
                if (directory == null || file == null) {
                    onDismissRequest()
                    return@LaunchedEffect
                }
                onFileChosen(directory + file)
                onDismissRequest()
            }
        }
    }
}

@Composable
fun LocalFileExplorerScreen() {
    Text("LOCAL FILE EXPLORER (in construction)")
}

@Composable
fun LdfsFileExplorerScreen() {
    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedFile by remember { mutableStateOf("") }
    var selectedDirectoryId by remember { mutableStateOf<UUID?>(null) }
    val coroutineScopeScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxHeight()) {
        Box(modifier = Modifier.weight(1f)) {
            Text(
                text = "LDFS FILE EXPLORER (in construction)",
            )
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.DarkGray),
        ) {
            if (selectedFile != "") {
                Text("Selected file: $selectedFile")
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        // TODO: upload file to chunk servers
                        coroutineScopeScope.launch {
                            isLoading = true
                            val fileCreationRequest =
                                createFileCreateRequest(
                                    filePath = selectedFile,
                                    directoryId = selectedDirectoryId ?: UUID.randomUUID(),
                                )
                            val chunkUploadRequest =
                                createChunkUploadRequest(
                                    originalFilePath = selectedFile,
                                    fileCreationRequest = fileCreationRequest,
                                )
                            for (req in chunkUploadRequest) {
                                uploadChunk(req)
                            }
                            isLoading = false
                        }
                    },
                    enabled = isLoading.not(),
                    modifier = Modifier.padding(4.dp),
                ) {
                    if (isLoading) {
                        Text("uploading...")
                    } else {
                        Text("upload")
                    }
                }
                Button(
                    onClick = { selectedFile = "" },
                    modifier = Modifier.padding(4.dp),
                ) {
                    Text("clear")
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { isDialogOpen = true },
                    modifier = Modifier.padding(4.dp),
                ) {
                    Text("Choose File")
                }
            }
        }
    }

    FileSelectorDialog(isDialogOpen, onFileChosen = { chosenFile ->
        selectedFile = chosenFile
        println("chosenFile: $chosenFile")
        isDialogOpen = false
    }, onDismissRequest = {
        isDialogOpen = false
    })
}

suspend fun callKtor() {
    val client = HttpClient(CIO)
    val response: HttpResponse = client.get("https://ktor.io/")
    println(response.status)
    client.close()
}
