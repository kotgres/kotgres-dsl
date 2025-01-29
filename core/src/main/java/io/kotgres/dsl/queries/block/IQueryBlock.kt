package io.kotgres.dsl.queries.block

interface IQueryBlock {
    fun getStatement(): String
    override fun toString(): String
}
