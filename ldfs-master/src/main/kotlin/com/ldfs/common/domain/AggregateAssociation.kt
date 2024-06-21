package com.ldfs.common.domain

open class AggregateAssociation<A : Aggregate<ID>, ID>(
    override var id: ID,
) : Association<A, ID>(
        id = id,
    )
