package com.ldfs.presentation

import com.ldfs.control.domain.model.entity.ChunkServerEntity
import com.ldfs.control.domain.service.ChunkServerAccessService
import com.ldfs.presentation.dto.request.HeartBeatRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chunkServer")
class ChunkServerController(
    private val chunkServerAccessService: ChunkServerAccessService,
) {
    @GetMapping
    fun chunkServers(): ResponseEntity<List<ChunkServerEntity>> {
        return ResponseEntity.ok().body(chunkServerAccessService.findAll())
    }

    @PostMapping("/heartBeat")
    fun heartBeatUpdateServerStatus(
        @RequestBody heartBeatRequest: HeartBeatRequest,
    ): ResponseEntity<ChunkServerEntity> {
        val storedEntity =
            chunkServerAccessService.makeServerDiscoverable(
                heartBeatRequest.ip!!,
                heartBeatRequest.port!!,
                heartBeatRequest.remainingStorageSize!!,
            )
        return ResponseEntity.ok().body(storedEntity)
    }
}
