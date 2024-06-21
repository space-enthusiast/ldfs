package com.ldfs.control.application.directory

import com.ldfs.control.domain.service.DirectoryCommandService
import com.ldfs.control.domain.service.DirectoryQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DirectoryDeleteService(
    private val commandService: DirectoryCommandService,
    private val queryService: DirectoryQueryService,
) {
    @Transactional
    fun delete(directoryId: UUID) {
        recursivelyDeleteDirectory(setOf(directoryId))
    }

    private tailrec fun recursivelyDeleteDirectory(directoryIds: Set<UUID>) {
        if (directoryIds.isEmpty()) {
            return
        }

        val lockedDirectories = queryService.queryWriteLock(directoryIds.toList())
        val childDirectories =
            queryService.query(
                parentDirectoryIds = lockedDirectories.mapNotNull { it.parent?.id }.toSet(),
            )

        commandService.delete(lockedDirectories.map { it.id }.toSet())

        recursivelyDeleteDirectory(childDirectories.map { it.id }.toSet())
    }
}
