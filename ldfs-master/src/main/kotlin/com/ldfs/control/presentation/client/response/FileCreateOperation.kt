package com.ldfs.control.presentation.client.response

import java.net.InetSocketAddress
import java.util.UUID

class FileCreateOperation(
    val storageNodeIpToByteSize: Map<InetSocketAddress, Pair<UUID, Long>>,
)
