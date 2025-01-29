package io.kotgres.dsl

data class ConflictSet(
    val columnName: String,
    val setExpression: Any?,
)