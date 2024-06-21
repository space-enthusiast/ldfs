package com.ldfs.presentation.dto.request

import java.util.UUID

class CreateFileRequest(
    val fileUuid: UUID,
    val fileSize: Long,
    val fileName: String,
    val directoryId: UUID,
)
