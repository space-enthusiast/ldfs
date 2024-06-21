package com.ldfs.control.presentation.client.request

class FileCreateOperationCreateRequest(
    val name: String,
    val replicationCnt: Int? = null,
    val byteSize: Long,
)
