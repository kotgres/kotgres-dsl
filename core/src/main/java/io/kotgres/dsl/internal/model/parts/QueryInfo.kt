package io.kotgres.dsl.internal.model.parts

internal data class QueryInfo(val sql: String, val columns: List<String>, val sqlFields: String, val parts: QueryParts)