package com.ldfs.control.domain.service

import com.ldfs.control.domain.model.entity.ChunkEntity
import com.ldfs.control.domain.model.entity.ChunkState
import com.ldfs.control.domain.repository.ChunkEntityRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.function.Consumer

@Service
class ChunkEntityMutationService(
    private val chunkEntityRepository: ChunkEntityRepository,
) {
    @Transactional
    fun save(chunkEntity: ChunkEntity): ChunkEntity {
        return chunkEntityRepository.save(chunkEntity)
    }

    @Transactional
    fun deleteFile(fileId: UUID) {
        // TODO
        val deleteList = chunkEntityRepository.findAllByFileUUID(fileId)
        if (deleteList.isEmpty()) {
            throw EntityNotFoundException("File with ID" + fileId + "is not found within DB")
        }
        deleteList.forEach(
            Consumer { chunkEntity: ChunkEntity ->
                chunkEntity.stateChunk = ChunkState.DELETING
            },
        )
    }
}
