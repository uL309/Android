package com.example.pokedex.data

data class Moves (
    val move: Move,
    val version_group_details: List<VersionGroupDetails>
){
    fun getMoveName(): String {
        return move.name
    }

    fun getLevelLearnedAt(): Int {
        return version_group_details.first().level_learned_at
    }

    fun getMoveLearnMethod(): String {
        return version_group_details.first().move_learn_method.name
    }

    fun getVersionGroup(): String {
        return version_group_details.first().version_group.name
    }
}

data class Move (
    val name: String,
    val url: String
)

data class VersionGroupDetails (
    val level_learned_at: Int,
    val move_learn_method: MoveLearnMethod,
    val version_group: VersionGroup
)

data class MoveLearnMethod (
    val name: String,
    val url: String
)

data class VersionGroup (
    val name: String,
    val url: String
)
