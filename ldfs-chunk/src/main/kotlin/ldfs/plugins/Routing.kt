package ldfs.plugins

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.readAllParts
import io.ktor.http.content.streamProvider
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.io.File
import java.util.UUID

fun Application.configureRouting() {
    val cashFolder = environment.config.property("cash-folder-path").getString()

    routing {
        get("/hello") {
            call.respondText("Hello World!")
        }

        post("/upload") {
//            val allPart = call.receiveMultipart().readAllParts()

//            val chunkUuid =
//                (
//                    allPart.firstOrNull {
//                        it is PartData.FormItem && it.name == "chunkUuid"
//                    } as PartData.FormItem?
//                )?.value?.let {
//                    runCatching { UUID.fromString(it) }.onFailure {
//                        call.respondText(
//                            text = "Chunk UUID wrong format",
//                            contentType = ContentType.Text.Plain,
//                            status = HttpStatusCode.BadRequest,
//                        )
//                        return@post
//                    }
//                } ?: run {
//                    call.respondText(
//                        text = "Chunk UUID not found",
//                        contentType = ContentType.Text.Plain,
//                        status = HttpStatusCode.BadRequest,
//                    )
//                    return@post
//                }

            call.receiveMultipart().forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        val chunkUuid = part.name.let { UUID.fromString(it) }
                        File("$cashFolder/${System.currentTimeMillis()}-$chunkUuid").also {
                            part.streamProvider().use { input ->
                                it.outputStream().buffered().use { output ->
                                    input.copyTo(output)
                                }
                            }
                        }
                    }
                    else -> {}
                }
            }
            call.respondText("File uploaded successfully", ContentType.Text.Plain)
        }

        post("/upload2") {
            val multipart = call.receiveMultipart()
//            var chunkUuid: UUID? = null

            multipart.forEachPart { part ->
                when (part) {
//                    is PartData.FormItem -> {
//                        println("FormItem received: ${part.name} = ${part.value}")
//                        if (part.name == "chunkUuid") {
//                            chunkUuid = UUID.fromString(part.value)
//                            println("Parsed chunkUuid: $chunkUuid")
//                        }
//                    }
                    is PartData.FileItem -> {
                        println("FileItem received: ${part.name}")
                        val chunkUuid = part.name.let { UUID.fromString(it) }
                        val file = chunkUuid?.let { uuid ->
                            File("$cashFolder/${System.currentTimeMillis()}-$uuid")
                        }
                        file?.also {
                            part.streamProvider().use { input ->
                                it.outputStream().buffered().use { output ->
                                    input.copyTo(output)
                                }
                                println("File stored at: ${it.path}")
                            }
                        } ?: println("chunkUuid is null, file not saved.")
                    }
                    else -> {
                        println("Other part received: $part")
                    }
                }
                part.dispose()
            }
            call.respondText("File uploaded successfully", ContentType.Text.Plain)
        }
    }
}
