package com.ldfs.control.domain.service

import com.ldfs.common.domain.AggregateAssociation
import com.ldfs.control.domain.common.toAggregate
import com.ldfs.control.domain.common.toEntity
import com.ldfs.control.domain.model.aggregate.Directory
import com.ldfs.control.domain.repository.DirectoryEntityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DirectoryCommandService(
    private val repository: DirectoryEntityRepository,
) {
    @Transactional
    fun create(
        name: String,
        parentDirectoryUuid: UUID?,
    ): Directory {
        return repository.save(
            Directory(
                name = name,
                parent =
                    parentDirectoryUuid?.let {
                        AggregateAssociation(it)
                    },
            ).toEntity(),
        ).toAggregate()
    }

    @Transactional
    fun save(directory: Directory): Directory {
        return repository.save(directory.toEntity()).toAggregate()
    }

    @Transactional
    fun saveAll(directories: List<Directory>): List<Directory> {
        return repository.saveAll(directories.map { it.toEntity() }).map { it.toAggregate() }
    }

    @Transactional
    fun delete(id: UUID) {
        repository.deleteById(id)
    }

    @Transactional
    fun delete(ids: Set<UUID>) {
        repository.deleteAllByIdInBatch(ids)
    }
}
