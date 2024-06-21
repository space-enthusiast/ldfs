package com.ldfs.control.domain.service

import com.ldfs.control.domain.model.entity.ChunkEntity
import com.ldfs.control.domain.model.entity.ChunkServerEntity
import com.ldfs.control.domain.model.entity.ServerState
import com.ldfs.control.domain.repository.ChunkEntityRepository
import com.ldfs.control.domain.repository.ChunkServerEntityRepository
import com.ldfs.presentation.dto.response.CreateFileResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.net.InetSocketAddress
import java.util.LinkedList
import java.util.UUID
import java.util.stream.IntStream

@Service
class ChunkServerAccessService(
    private val chunkServerEntityRepository: ChunkServerEntityRepository,
    private val chunkEntityRepository: ChunkEntityRepository,
) {
    private val chunkSize = 64L

    fun findServersWithChunkMetadata(
        fileId: UUID?,
        sequence: Long?,
    ): List<ChunkServerEntity> {
        val serverIdsWithChunk: Set<UUID?> =
            HashSet(
                chunkEntityRepository
                    .findAllByFileUUIDAndChunkOrder(fileId!!, sequence!!)
                    .stream()
                    .map(ChunkEntity::chunkServerId)
                    .toList(),
            )
        return chunkServerEntityRepository.findAllById(serverIdsWithChunk)
    }

    @Deprecated("")
    fun findServersWithFile(fileId: UUID?): List<ChunkServerEntity> {
        val serverIdsWithChunk: Set<UUID?> =
            HashSet(
                chunkEntityRepository
                    .findAllByFileUUID(fileId!!)
                    .stream()
                    .map(ChunkEntity::chunkServerId)
                    .toList(),
            )
        return chunkServerEntityRepository.findAllById(serverIdsWithChunk)
    }

    fun findServerWithSpecificChunk(chunk: ChunkEntity): ChunkServerEntity {
        return chunkServerEntityRepository.findById(chunk.chunkServerId!!).orElseThrow()
    }

    fun findServerWithInetSocketAddr(addr: InetSocketAddress): ChunkServerEntity? {
        return chunkServerEntityRepository.findByIp(addr.address.hostAddress)
    }

    @Transactional
    fun findServersWithStorageSpace(numberOfChunks: Int): List<CreateFileResponse> {
        val chunkServerEntities: MutableList<ChunkServerEntity> = LinkedList()
        for (i in 0 until numberOfChunks) {
            val chunkServerEntity =
                chunkServerEntityRepository.findByLargestRemainingStorageSize().firstOrNull()
                    ?: throw Exception("no chunk server available")
            chunkServerEntities.add(chunkServerEntity)
            chunkServerEntity.remainingStorageSize = chunkServerEntity.remainingStorageSize - chunkSize
            chunkServerEntityRepository.save(chunkServerEntity)
        }
        return IntStream.range(0, chunkServerEntities.size).mapToObj { idx: Int ->
            CreateFileResponse(
                idx.toLong(),
                chunkServerEntities[idx].ip,
                chunkServerEntities[idx].port,
            )
        }
            .toList()
    }

    fun makeServerDiscoverable(
        ip: String,
        port: String,
        remainingStorageSize: Long,
    ): ChunkServerEntity {
        return chunkServerEntityRepository.findByIpAndPort(ip, port)
            ?: chunkServerEntityRepository.save(
                ChunkServerEntity(
                    id = UUID.randomUUID(),
                    ip = ip,
                    port = port,
                    remainingStorageSize = remainingStorageSize,
                    serverState = ServerState.NORMAL,
                ),
            )
    }

    fun findAll(): List<ChunkServerEntity> {
        return chunkServerEntityRepository.findAll()
    }
}
