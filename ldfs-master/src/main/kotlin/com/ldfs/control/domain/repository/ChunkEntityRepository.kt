package com.ldfs.control.domain.repository

import com.ldfs.control.domain.model.entity.ChunkEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ChunkEntityRepository : JpaRepository<ChunkEntity, UUID> {
    fun findAllByFileUUID(FileUUID: UUID): List<ChunkEntity>

    fun findAllByFileUUIDAndChunkOrder(
        FileUUID: UUID,
        chunkOrder: Long,
    ): List<ChunkEntity>

    fun deleteAllByFileUUID(FileUUID: UUID): List<ChunkEntity>
}
