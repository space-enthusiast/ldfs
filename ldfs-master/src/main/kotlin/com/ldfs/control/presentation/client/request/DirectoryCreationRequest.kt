package com.ldfs.control.presentation.client.request

import java.util.UUID

class DirectoryCreationRequest(
    val name: String,
    val parentUuid: UUID? = null,
)
