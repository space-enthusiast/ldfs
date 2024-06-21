package com.ldfs.control.presentation.client

import com.ldfs.control.application.directory.DirectoryCreateService
import com.ldfs.control.application.directory.DirectoryDeleteService
import com.ldfs.control.application.directory.DirectoryUpdateService
import com.ldfs.control.domain.model.aggregate.Directory
import com.ldfs.control.domain.model.aggregate.File
import com.ldfs.control.presentation.client.request.DirectoryCreationRequest
import com.ldfs.control.presentation.client.request.DirectoryUpdateRequest
import com.ldfs.control.presentation.client.request.FileCreateOperationCreateRequest
import com.ldfs.control.presentation.client.request.FileUpdateOperationCreateRequest
import com.ldfs.control.presentation.client.request.FileUpdateRequest
import com.ldfs.control.presentation.client.response.FileCreateOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RequestMapping("client")
@RestController
class ClientRestController(
    private val directoryCreateService: DirectoryCreateService,
    private val directoryUpdateService: DirectoryUpdateService,
    private val directoryDeleteService: DirectoryDeleteService,
) {
    @PostMapping("directory")
    fun createDirectory(
        @RequestBody request: DirectoryCreationRequest,
    ): ResponseEntity<Directory> {
        return ResponseEntity(
            directoryCreateService.create(
                name = request.name,
                parentDirectoryId = request.parentUuid,
            ),
            HttpStatus.CREATED,
        )
    }

    @PatchMapping("directory/{directoryUuid}")
    fun updateDirectory(
        @PathVariable("directoryUuid") directoryUuid: UUID,
        @RequestBody request: DirectoryUpdateRequest,
    ): ResponseEntity<Directory> {
        return ResponseEntity.ok(
            directoryUpdateService.update(
                directoryId = directoryUuid,
                name = request.name,
                parentDirectoryId = request.parentDirectoryUuid,
            ),
        )
    }

    @DeleteMapping("directory/{directoryUuid}")
    fun deleteDirectory(
        @PathVariable("directoryUuid") directoryUuid: UUID,
    ): ResponseEntity<Any> {
        directoryDeleteService.delete(directoryUuid)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("directory/{directoryUuid}/file/operation")
    fun createFileCreateOperation(
        @PathVariable("directoryUuid") directoryUuid: UUID,
        @RequestBody request: FileCreateOperationCreateRequest,
    ): ResponseEntity<FileCreateOperation> {
        TODO()
    }

    @PatchMapping("file/{fileUuid}/metadata")
    fun updateFileMetadata(
        @PathVariable("fileUuid") fileUuid: UUID,
        @RequestBody request: FileUpdateRequest,
    ): ResponseEntity<File> {
        TODO()
    }

    @PostMapping("file/{fileUuid}/operation")
    fun createFileUpdateOperation(
        @PathVariable("fileUuid") fileUuid: UUID,
        @RequestBody request: FileUpdateOperationCreateRequest,
    ): ResponseEntity<FileCreateOperation> {
        TODO()
    }

    @DeleteMapping("file/{fileUuid}")
    fun deleteFile(
        @PathVariable("fileUuid") fileUuid: UUID,
    ): ResponseEntity<Any> {
        TODO()
    }
}
