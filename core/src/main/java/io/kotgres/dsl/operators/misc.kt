package io.kotgres.dsl.operators

import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.block.QueryBlock

/**
 * PARENTHESIS
 */

fun sub(value: IQueryBlock): IQueryBlock {
    return QueryBlock("(", value.getStatement(), ")")
}

fun sub(value: String): IQueryBlock {
    return QueryBlock("(", value, ")")
}

fun par(value: IQueryBlock): IQueryBlock {
    return QueryBlock("(", value.getStatement(), ")")
}

fun par(value: String): IQueryBlock {
    return QueryBlock("( ", value, ")")
}

/**
 * FULL TEXT SEARCH
 */

fun toTsvector(value: String, config: String? = null): IQueryBlock {
    // todo escape config
    return QueryBlock("to_tsvector(${if (config != null) "'$config', " else ""}'''$value''')")
}

/**
 * RENAMING
 */

infix fun String.AS(value: String): IQueryBlock {
    return QueryBlock(this, "AS", value)
}

infix fun IQueryBlock.AS(value: String): IQueryBlock {
    return QueryBlock(this.getStatement(), "AS", value)
}