package com.ldfs.control.domain.service.leaderElection

import com.ldfs.control.domain.model.entity.ChunkServerEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestLeaderElectionController(private val leaderElectionRequestService: LeaderElectionRequestService) {
    @PostMapping("/initiate-election")
    fun initiateElection(
        @RequestBody chunkServers: List<ChunkServerEntity>,
        @RequestParam checksum: String?,
    ): ResponseEntity<Void> {
        chunkServers.forEach { chunkServer ->
            leaderElectionRequestService.httpLeaderElectionService(chunkServer, chunkServers, checksum)
        }
        return ResponseEntity.ok().build()
    }
}
