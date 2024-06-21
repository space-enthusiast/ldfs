package com.ldfs.presentation.dto.response

data class CreateFileResponse(
    var order: Long,
    var chunkServerIP: String,
    var chunkServerPort: String,
)
