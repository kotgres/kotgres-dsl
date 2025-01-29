package io.kotgres.dsl.internal.model.parts

internal data class SelectPart (
    val columns: List<String>,
    val sqlFields: String
)
