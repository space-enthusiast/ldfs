package com.ldfs.control.domain.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.OffsetDateTime
import java.util.UUID

@Entity(name = "directory")
class DirectoryEntity(
    @Id
    var id: UUID,
    val created: OffsetDateTime,
    val updated: OffsetDateTime?,
    var name: String,
    var parent: UUID?,
)
