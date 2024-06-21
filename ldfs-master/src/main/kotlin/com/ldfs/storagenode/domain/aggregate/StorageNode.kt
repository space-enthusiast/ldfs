package com.ldfs.storagenode.domain.aggregate

import com.ldfs.common.domain.Aggregate
import java.net.InetSocketAddress
import java.time.OffsetDateTime
import java.util.UUID

class StorageNode(
    override var id: UUID,
    override val created: OffsetDateTime = OffsetDateTime.now(),
    override val updated: OffsetDateTime? = null,
    val ip: InetSocketAddress,
    val alive: Boolean,
) : Aggregate<UUID>(
        id = id,
        created = created,
        updated = updated,
    )
