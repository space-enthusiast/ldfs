package com.ldfs.control.domain.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.util.UUID

@Entity
class ChunkServerEntity(
    @Id
    val id: UUID,
    val ip: String,
    val port: String,
    var remainingStorageSize: Long = 0L,
    @Enumerated(EnumType.STRING)
    var serverState: ServerState = ServerState.UNKNOWN,
)
