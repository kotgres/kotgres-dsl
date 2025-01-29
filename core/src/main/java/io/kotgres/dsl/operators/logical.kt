package io.kotgres.dsl.operators

import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.block.QueryBlock

infix fun IQueryBlock.and(value: IQueryBlock): IQueryBlock {
    return QueryBlock(this.toString(), "AND", value.toString())
}

infix fun IQueryBlock.or(value: IQueryBlock): IQueryBlock {
    return QueryBlock(this.toString(), "OR", value.toString())
}
