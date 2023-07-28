package it.codeclub.pokeclub.remote.data

data class EffectChange(
    val effect_entries: List<EffectEntry>,
    val version_group: VersionGroupX
)