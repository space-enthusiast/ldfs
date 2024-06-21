package com.ldfs.control.domain.model.aggregate

import com.ldfs.common.domain.Aggregate
import com.ldfs.common.domain.AggregateAssociation
import java.time.OffsetDateTime
import java.util.UUID

class Directory(
    override var id: UUID = UUID.randomUUID(),
    override val created: OffsetDateTime = OffsetDateTime.now(),
    override val updated: OffsetDateTime? = null,
    var name: String,
    var parent: AggregateAssociation<Directory, UUID>?,
) : Aggregate<UUID>(
        id = id,
        created = created,
        updated = updated,
    )
