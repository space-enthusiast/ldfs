package com.ldfs.control.domain.repository

import com.ldfs.control.domain.model.entity.ChunkServerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ChunkServerEntityRepository : JpaRepository<ChunkServerEntity, UUID> {
    fun findByIp(ipAddress: String): ChunkServerEntity?

    fun findByIpAndPort(
        ipAddress: String,
        port: String,
    ): ChunkServerEntity?

    @Query(
        "SELECT i FROM ChunkServerEntity i " +
            "WHERE i.remainingStorageSize = (SELECT MAX(i.remainingStorageSize) FROM ChunkServerEntity i)",
    )
    fun findByLargestRemainingStorageSize(): List<ChunkServerEntity>
}
