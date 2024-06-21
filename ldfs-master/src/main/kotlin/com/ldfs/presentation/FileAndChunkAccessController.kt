package com.ldfs.presentation

import AppendUpdateResponse
import com.ldfs.control.domain.model.entity.ChunkEntity
import com.ldfs.control.domain.service.ChunkAccessService
import com.ldfs.control.domain.service.ChunkEntityMutationService
import com.ldfs.control.domain.service.FileAccessService
import com.ldfs.control.domain.service.leaderElection.ChunkLeaderElectionService
import com.ldfs.presentation.dto.request.CreateFileRequest
import com.ldfs.presentation.dto.response.CreateFileResponse
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/files")
class FileAndChunkAccessController
    @Autowired
    constructor(
        private val chunkAccessService: ChunkAccessService,
        private val chunkEntityMutationService: ChunkEntityMutationService,
        private val leaderElectionService: ChunkLeaderElectionService,
        private val fileAccessService: FileAccessService,
    ) {
        @GetMapping("/getFile")
        fun getFile(
            @RequestParam("fileId") fileId: UUID?,
        ): ResponseEntity<List<ChunkEntity>> {
            // Logic to retrieve file chunks based on fileId
            // Dummy response for demonstration
            val response = chunkAccessService.getFileChunks(fileId)
            // Populate response with dummy data
            return ResponseEntity.ok(response)
        }

        @Transactional
        @PostMapping("/fileCreateOperation")
        fun createFile(
            @RequestBody request: CreateFileRequest,
        ): ResponseEntity<List<CreateFileResponse>> {
            fileAccessService.save(
                fileUuid = request.fileUuid,
                fileName = request.fileName,
                directoryId = request.directoryId,
            )
            return ResponseEntity.ok(
                fileAccessService.chunkify(request.fileSize),
            )
        }

        @DeleteMapping("/deleteFile")
        fun deleteFile(
            @RequestParam("fileId") fileId: UUID?,
        ): ResponseEntity<String> {
            // Logic to delete a file
            // Dummy response for demonstration
            chunkEntityMutationService.deleteFile(fileId!!)
            return ResponseEntity.ok("File deleted successfully")
        }

        // TODO
        @PostMapping("/appendUpdateFile")
        fun appendUpdateFile(
            @RequestParam fileId: UUID?,
            @RequestParam ChunkId: Long?,
        ): ResponseEntity<AppendUpdateResponse> {
            // Logic to append or update a file
            val candidates = chunkAccessService.getSpecificChunkOfFile(fileId, ChunkId)
            val result = leaderElectionService.electLeader(candidates)
            // Dummy response for demonstration
            val response: AppendUpdateResponse = AppendUpdateResponse(result.nonLeased, result.leased)
            // Populate response with dummy data
            return ResponseEntity.ok<AppendUpdateResponse>(response)
        }
    }
