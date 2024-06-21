package com.ldfs.common.domain

import java.time.OffsetDateTime

open class Aggregate<ID>(
    override var id: ID,
    open val created: OffsetDateTime = OffsetDateTime.now(),
    open val updated: OffsetDateTime? = null,
) : Identifiable<ID>(
        id = id,
    )
