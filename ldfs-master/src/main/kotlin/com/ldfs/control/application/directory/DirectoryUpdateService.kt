package com.ldfs.control.application.directory

import com.ldfs.common.domain.AggregateAssociation
import com.ldfs.control.domain.model.aggregate.Directory
import com.ldfs.control.domain.service.DirectoryCommandService
import com.ldfs.control.domain.service.DirectoryQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DirectoryUpdateService(
    private val commandService: DirectoryCommandService,
    private val queryService: DirectoryQueryService,
) {
    @Transactional
    fun update(
        directoryId: UUID,
        name: String?,
        parentDirectoryId: UUID?,
    ): Directory {
        validateUpdatePossible(
            name = name,
            parentDirectoryId = parentDirectoryId,
        )
        val (lockedDirectory, lockedParentDirectory) =
            queryAndLockDirectoryAndParentDirectory(
                directoryId = directoryId,
                parentDirectoryId = parentDirectoryId,
            )

        lockedParentDirectory?.let {
            lockedDirectory.parent = AggregateAssociation(it.id)
        }

        name?.let {
            lockedDirectory.name = it
        }

        return commandService.save(lockedDirectory)
    }

    private fun queryAndLockDirectoryAndParentDirectory(
        directoryId: UUID,
        parentDirectoryId: UUID?,
    ): Pair<Directory, Directory?> {
        return if (parentDirectoryId == null) {
            Pair(
                queryService.queryReadLock(directoryId),
                null,
            )
        } else {
            val lockedDirectories = queryService.queryReadLock(listOf(directoryId, parentDirectoryId))
            Pair(
                lockedDirectories[0],
                lockedDirectories[1],
            )
        }
    }

    private fun validateUpdatePossible(
        name: String?,
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
