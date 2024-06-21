package com.ldfs.control.application.directory

import com.ldfs.control.domain.model.aggregate.Directory
import com.ldfs.control.domain.service.DirectoryCommandService
import com.ldfs.control.domain.service.DirectoryQueryService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DirectoryCreateService(
    private val commandService: DirectoryCommandService,
    private val queryService: DirectoryQueryService,
) {
    fun create(
        name: String,
        parentDirectoryId: UUID?,
    ): Directory {
        validateCreatePossible(
            name = name,
            parentDirectoryId = parentDirectoryId,
        )
        return commandService.create(
            name = name,
            parentDirectoryUuid = parentDirectoryId,
        )
    }

    private fun validateCreatePossible(
        name: String,
        parentDirectoryId: UUID?,
    ) {
        if (name == "") {
            throw RuntimeException("directory name should not be blank")
        }

        parentDirectoryId?.let {
            queryService.query(id = parentDirectoryId)
        }
    }
}
