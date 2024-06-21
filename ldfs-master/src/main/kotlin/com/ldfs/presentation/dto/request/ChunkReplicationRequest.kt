package com.ldfs.presentation.dto.request

import java.util.UUID

class ChunkReplicationRequest(
    var chunkUUID: UUID?,
    var chunkServerIp: String?,
    var chunkServerPort: String?,
    var fileId: UUID? = null,
    var chunkOrder: Long? = null,
)
