import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import java.io.File
import java.util.UUID

@Serializable
data class FileCreateOperationCreationRequest(
    @Serializable(with = UUIDSerializer::class)
    val fileUuid: UUID,
    val fileSize: Long,
    val fileName: String,
    @Serializable(with = UUIDSerializer::class)
    val directoryId: UUID,
)

@Serializable
data class ChunkCreationRequest(
    val order: Long,
    val chunkServerIP: String,
    val chunkServerPort: String,
)

data class FileCreationOperation(
    val fileUuid: UUID,
    val requests: List<ChunkCreationRequest>,
)

suspend fun createFileCreateRequest(
    filePath: String,
    directoryId: UUID,
): FileCreationOperation {
    val file = File(filePath)
    val fileSize = file.length()
    val fileName = file.name
    val fileUuid = UUID.randomUUID()
    return FileCreationOperation(
        fileUuid = fileUuid,
        requests =
            send(
                fileSize = fileSize,
                fileName = fileName,
                directoryId = directoryId,
                fileUuid = fileUuid,
            ),
    )
}

private suspend fun send(
    fileUuid: UUID,
    fileSize: Long,
    fileName: String,
    directoryId: UUID,
): List<ChunkCreationRequest> {
    val masterServerAddress = System.getProperty("masterServerAddress")
    val client =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
            expectSuccess = true
        }
    val response: HttpResponse =
        client.post("http://$masterServerAddress/api/files/fileCreateOperation") {
            contentType(ContentType.Application.Json)
            setBody(
                FileCreateOperationCreationRequest(
                    fileUuid = fileUuid,
                    fileSize = fileSize,
                    fileName = fileName,
                    directoryId = directoryId,
                ),
            )
        }
    client.close()
    return response.body<List<ChunkCreationRequest>>().also {
        println("res: $it")
    }
}

data class ChunkUploadRequest(
    val chunkServerIp: String,
    val chunkServerPort: String,
    val chunkUuid: UUID,
    val filePath: String,
)

suspend fun createChunkUploadRequest(
    originalFilePath: String,
    fileCreationRequest: FileCreationOperation,
): List<ChunkUploadRequest> {
    val chunkUuid = UUID.randomUUID()
    val firstFileCreationRequest = fileCreationRequest.requests.first()
    return listOf(
        ChunkUploadRequest(
            chunkServerIp = firstFileCreationRequest.chunkServerIP,
            chunkServerPort = firstFileCreationRequest.chunkServerPort,
            chunkUuid = chunkUuid,
            filePath = originalFilePath,
        ),
    )
}

suspend fun uploadChunk(request: ChunkUploadRequest) {
    val client =
        HttpClient(CIO) {
            expectSuccess = true
        }
    val file = File(request.filePath)
    client.submitFormWithBinaryData(
        url = "http://${request.chunkServerIp}:${request.chunkServerPort}/upload",
        formData =
            formData {
                append("", file.readBytes())
                append("chunkUuid", request.chunkUuid.toString())
            },
    )
}
