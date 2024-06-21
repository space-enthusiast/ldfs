package com.ldfs.control.domain.service.leaderElection

import com.ldfs.control.domain.model.entity.ChunkServerEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class LeaderElectionRequestService(private val restTemplate: RestTemplate) {
    private class RequestBodyDTO(var chunkServerEntities: List<ChunkServerEntity>, var checksum: String?)

    @Async
    fun httpLeaderElectionService(
        chunkServer: ChunkServerEntity,
        broadCastable: List<ChunkServerEntity>,
        checksum: String?,
    ) {
        val url = "http://" + chunkServer.ip + ":" + chunkServer.port + "/"
        val requestBody = RequestBodyDTO(broadCastable, checksum)
        restTemplate.postForObject(url, requestBody, Void::class.java)
    }
}
