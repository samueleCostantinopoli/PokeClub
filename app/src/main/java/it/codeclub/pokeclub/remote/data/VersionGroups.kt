package it.codeclub.pokeclub.remote.data

data class VersionGroups(
    val count: Int,
    val next: String?,
    val previous: Any,
    val results: List<Result>
)