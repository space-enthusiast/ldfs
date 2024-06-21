import com.ldfs.control.domain.model.entity.ChunkServerEntity

data class AppendUpdateResponse(
    var nonleasedServer: List<ChunkServerEntity>,
    var leasedServer: ChunkServerEntity,
)
