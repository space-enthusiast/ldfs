package com.ldfs.presentation.dto

import com.ldfs.control.domain.model.entity.ChunkServerEntity

data class LeaderFollowerChunkServers(
    val leased: ChunkServerEntity,
    val nonLeased: List<ChunkServerEntity>,
)
