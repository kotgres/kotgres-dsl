package io.kotgres.dsl.operators

import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.block.QueryBlock

fun distinct(column: String): IQueryBlock {
    return QueryBlock("DISTINCT", column)
}
