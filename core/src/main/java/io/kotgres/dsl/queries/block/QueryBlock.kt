package io.kotgres.dsl.queries.block


class QueryBlock(vararg statement: String) : IQueryBlock {

    val statement: List<String> = statement.toList()

    override fun getStatement(): String = statement.joinToString(" ")

    override fun toString(): String {
        return getStatement()
    }
}