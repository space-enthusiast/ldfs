package com.ldfs.control.domain.model.entity

import com.ldfs.control.domain.model.common.FileStatus
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.util.UUID

@Entity(name = "file")
class FileEntity(
    @Id
    val uuid: UUID,
    var name: String,
    var directoryId: UUID,
    @Enumerated(EnumType.STRING)
    var status: FileStatus = FileStatus.CREATING,
)
