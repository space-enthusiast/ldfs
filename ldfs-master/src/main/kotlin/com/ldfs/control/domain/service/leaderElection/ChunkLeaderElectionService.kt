package com.ldfs.control.domain.service.leaderElection

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.ldfs.control.domain.model.entity.ChunkEntity
import com.ldfs.control.domain.model.entity.ChunkServerEntity
import com.ldfs.control.domain.model.entity.ChunkState
import com.ldfs.control.domain.service.ChunkServerAccessService
import com.ldfs.presentation.dto.LeaderFollowerChunkServers
import org.springframework.stereotype.Service
import java.net.InetSocketAddress
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Base64
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class ChunkLeaderElectionService(
    private val chunkServerAccessService: ChunkServerAccessService,
    private val leaderElectionRequestService: LeaderElectionRequestService,
) {
    private val activeLeaderElections: Cache<String?, Pair<String, ChunkServerEntity?>> =
        Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build()

    // ------------------------------------------------------------

    /*
        ongoing request leader election request checksum to state or value
     */
    private val successState = "SUCCESS"
    private val waitingState = "WAITING"

    //    initiate leader election
    fun sendAsyncLeaderElectionRequest(broadCastList: List<ChunkServerEntity>): String? {
        val requestCheckSum = generateChecksum(broadCastList)
        for (i in broadCastList.indices) {
            leaderElectionRequestService.httpLeaderElectionService(broadCastList[i], broadCastList, requestCheckSum)
        }

        val leaderElectionDetails = Pair<String, ChunkServerEntity?>(waitingState, null)
        activeLeaderElections.put(requestCheckSum, leaderElectionDetails)

        return requestCheckSum
    }

    fun getLeaderElectedChunkServer(requestCheckSum: String?): Pair<String, ChunkServerEntity?> {
        return activeLeaderElections.getIfPresent(requestCheckSum)!!
    }

    // api that the chunk server uses to declare itself the leader
    fun setChunkServerAsLeader(
        chunkServer: InetSocketAddress?,
        chunkUuid: UUID?,
        requestCheckSum: String?,
    ) {
        val state = activeLeaderElections.getIfPresent(requestCheckSum)
        if (state != null && state.first == waitingState) {
            val chunkServerEntity = chunkServerAccessService.findServerWithInetSocketAddr(chunkServer!!)
            val newState = Pair(successState, chunkServerEntity)
            activeLeaderElections.put(requestCheckSum, newState)
        }
    }

    fun electLeader(candidates: List<ChunkEntity>): LeaderFollowerChunkServers {
        return extractLeaderAndLeash(
            candidates = candidates,
            algorithm = LeaderElectionAlgorithm.TEMPORARY_LEADER_ELECT_ALGORITHM,
        )
    }

    private fun generateChecksum(data: List<ChunkServerEntity>): String? {
        try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(data.toString().toByteArray())
            return Base64.getEncoder().encodeToString(hash)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            // Handle exception
            return null
        }
    }

    private fun leaderElectionChunkStateLock(candidates: List<ChunkEntity>) {
        for (candidate in candidates) {
            candidate.stateChunk = ChunkState.ELECTING
        }
    }

    private fun extractLeaderAndLeash(
        candidates: List<ChunkEntity>,
        algorithm: LeaderElectionAlgorithm,
    ): LeaderFollowerChunkServers {
        val chunkServersToChunks =
            candidates.map {
                chunkServerAccessService.findServerWithSpecificChunk(it) to it
            }
        return when (algorithm) {
            LeaderElectionAlgorithm.TEMPORARY_LEADER_ELECT_ALGORITHM -> {
                val leaderIdx = (Math.random() * chunkServersToChunks.size).toInt()
                LeaderFollowerChunkServers(
                    leased = chunkServersToChunks[leaderIdx].first,
                    nonLeased = chunkServersToChunks.filterIndexed { index, _ -> index != leaderIdx }.map { it.first },
                )
            }
            LeaderElectionAlgorithm.BALANCED_LEADER_ELECT_ALGORITHM -> {
                val chunkServerBroadCastList =
                    chunkServersToChunks.map {
                        it.first
                    }
                leaderElectionChunkStateLock(candidates)
                val checksum = sendAsyncLeaderElectionRequest(chunkServerBroadCastList)
                val (_, b) = getLeaderElectedChunkServer(checksum)
                LeaderFollowerChunkServers(
                    leased = b!!,
                    nonLeased = chunkServerBroadCastList.filter { it != b },
                )
            }
        }
    }
}

enum class LeaderElectionAlgorithm {
    TEMPORARY_LEADER_ELECT_ALGORITHM,
    BALANCED_LEADER_ELECT_ALGORITHM,
}
