package com.ldfs.control.presentation.client.request

import java.util.UUID

class FileUpdateRequest(
    val directoryUuid: UUID? = null,
    val name: String? = null,
)
