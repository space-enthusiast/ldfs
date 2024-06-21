package com.ldfs.control.domain.service

import com.ldfs.common.domain.AggregateNotFoundException
import com.ldfs.control.domain.common.toAggregate
import com.ldfs.control.domain.model.aggregate.Directory
import com.ldfs.control.domain.repository.DirectoryEntityRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DirectoryQueryService(
    private val repository: DirectoryEntityRepository,
) {
    fun query(
        id: UUID? = null,
        ids: Set<UUID>? = null,
        parentDirectoryId: UUID? = null,
        parentDirectoryIds: Set<UUID>? = null,
    ): List<Directory> {
        return repository.findAll(
            id = id,
            ids = ids,
            parentDirectoryId = parentDirectoryId,
            parentDirectoryIds = parentDirectoryIds,
        ).map { it.toAggregate() }
    }

    fun query(id: UUID): Directory {
        return repository.findByIdOrNull(id)?.toAggregate()
            ?: throw AggregateNotFoundException("directory not found id: $id")
    }

    fun queryReadLock(id: UUID): Directory {
        return repository.findAndPessimisticReadLockByIdOrNull(id)?.toAggregate()
            ?: throw AggregateNotFoundException(Directory::class.simpleName)
    }

    fun queryReadLock(ids: List<UUID>): List<Directory> {
        return repository.findAndPessimisticReadLockByIdsOrNull(ids).map {
            it.toAggregate()
        }.sortedBy { ids.indexOf(it.id) }.also { directories ->
            val inputIds = ids.toSet()
            val queriesIds = directories.map { it.id }.toSet()
            if (inputIds != queriesIds) {
                throw AggregateNotFoundException("${Directory::class.simpleName} ids: ${inputIds - queriesIds}")
            }
        }
    }

    fun queryWriteLock(id: UUID): Directory {
        return repository.findAndPessimisticWriteLockByIdOrNull(id)?.toAggregate()
            ?: throw AggregateNotFoundException(Directory::class.simpleName)
    }

    fun queryWriteLock(ids: List<UUID>): List<Directory> {
        return repository.findAndPessimisticWriteByIdsOrNull(ids).map {
            it.toAggregate()
        }.sortedBy { ids.indexOf(it.id) }.also { directories ->
            val inputIds = ids.toSet()
            val queriesIds = directories.map { it.id }.toSet()
            if (inputIds != queriesIds) {
                throw AggregateNotFoundException("${Directory::class.simpleName} ids: ${inputIds - queriesIds}")
            }
        }
    }
}
