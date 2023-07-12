package it.codeclub.pokeclub.remote.data

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)