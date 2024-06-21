package com.ldfs.control.domain.service

import com.ldfs.control.domain.model.entity.ChunkEntity
import com.ldfs.control.domain.repository.ChunkEntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChunkAccessService
    @Autowired
    constructor(private val chunkEntityRepository: ChunkEntityRepository) {
        fun getFileChunks(fileId: UUID?): List<ChunkEntity> {
            return chunkEntityRepository.findAllByFileUUID(fileId!!)
        }

        fun getSpecificChunkOfFile(
            fileId: UUID?,
            chunkId: Long?,
        ): List<ChunkEntity> {
            return chunkEntityRepository.findAllByFileUUID(fileId!!)
        }
    }
