package com.ldfs.control.domain.common

import com.ldfs.common.domain.AggregateAssociation
import com.ldfs.control.domain.model.aggregate.Directory
import com.ldfs.control.domain.model.entity.DirectoryEntity

fun Directory.toEntity(): DirectoryEntity {
    return DirectoryEntity(
        id = this.id,
        created = this.created,
        updated = this.updated,
        name = this.name,
        parent = this.parent?.id,
    )
}

fun DirectoryEntity.toAggregate(): Directory {
    return Directory(
        id = this.id,
        created = this.created,
        updated = this.updated,
        name = this.name,
        parent = this.parent?.let { AggregateAssociation(it) },
    )
}
