package com.ldfs.control.domain.service

import com.ldfs.control.domain.model.entity.FileEntity
import com.ldfs.control.domain.repository.FileEntityRepository
import com.ldfs.presentation.dto.response.CreateFileResponse
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FileAccessService(
    private val fileEntityRepository: FileEntityRepository,
    private val chunkServerAccessService: ChunkServerAccessService,
) {
    companion object {
        private const val CHUNK_SIZE_IN_BYTES = 67108864L
    }

    fun save(
        fileUuid: UUID,
        directoryId: UUID,
        fileName: String,
    ): FileEntity {
        return fileEntityRepository.save(
            FileEntity(
                uuid = fileUuid,
                name = fileName,
                directoryId = directoryId,
            ),
        )
    }

    fun chunkify(fileSize: Long): List<CreateFileResponse> {
        val totalChunkNumber = (fileSize / CHUNK_SIZE_IN_BYTES).toInt() + 1
        return chunkServerAccessService.findServersWithStorageSpace(totalChunkNumber)
    }
}
