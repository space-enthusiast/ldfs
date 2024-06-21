package com.ldfs.presentation

import com.ldfs.presentation.dto.request.ChunkReplicationRequest
import jakarta.transaction.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/files/replication")
class FileReplicationController {
    @PostMapping("/chunkReplicationInit")
    @Transactional
    fun chunkReplicationInit(
        @RequestBody chunkReplicationRequest: ChunkReplicationRequest?,
    ) {
    }
}
