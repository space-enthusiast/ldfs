package com.ldfs.control.domain.model.aggregate

import com.ldfs.common.domain.Aggregate
import com.ldfs.common.domain.AggregateAssociation
import java.time.OffsetDateTime
import java.util.UUID

data class File(
    override var id: UUID,
    override val created: OffsetDateTime = OffsetDateTime.now(),
    override val updated: OffsetDateTime? = null,
    var name: String,
    var directory: AggregateAssociation<Directory, UUID>,
) : Aggregate<UUID>(
        id = id,
        created = created,
        updated = updated,
    )
