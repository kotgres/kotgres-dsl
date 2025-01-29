package dsl.operators

import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.block.QueryBlock

fun randomOperator(name: String): IQueryBlock {
    return QueryBlock("RANDOM($name)")
}