package it.codeclub.pokeclub.remote.data

data class HeldItem(
    val item: Item,
    val version_details: List<VersionDetail>
)