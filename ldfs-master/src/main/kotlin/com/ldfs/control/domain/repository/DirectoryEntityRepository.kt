package com.ldfs.control.domain.repository

import com.ldfs.control.domain.model.entity.DirectoryEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DirectoryEntityRepository : JpaRepository<DirectoryEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select d from directory d")
    fun findAndPessimisticReadLockByIdOrNull(id: UUID): DirectoryEntity?

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select d from directory d where d.id in :ids")
    fun findAndPessimisticReadLockByIdsOrNull(
        @Param("ids") ids: List<UUID>,
    ): List<DirectoryEntity>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select d from directory d")
    fun findAndPessimisticWriteLockByIdOrNull(id: UUID): DirectoryEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select d from directory d where d.id in :ids")
    fun findAndPessimisticWriteByIdsOrNull(
        @Param("ids") ids: List<UUID>,
    ): List<DirectoryEntity>

    @Query(
        """
        select d from directory d
        where (:id is null or d.id = :id) 
        and (:ids is null or d.id in :ids) 
        and (:parentDirectoryId is null or d.parent = :parentDirectoryId)
        and (:parentDirectoryIds is null or d.parent in :parentDirectoryIds)
    """,
    )
    fun findAll(
        @Param("id") id: UUID?,
        @Param("ids") ids: Set<UUID>?,
        @Param("parentDirectoryId") parentDirectoryId: UUID?,
        @Param("parentDirectoryIds") parentDirectoryIds: Set<UUID>?,
    ): List<DirectoryEntity>
}
