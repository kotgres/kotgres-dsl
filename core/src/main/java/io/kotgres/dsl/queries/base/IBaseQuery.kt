package io.kotgres.dsl.queries.base

interface IBaseQuery {
    fun toSql(shouldLineBreak: Boolean = false): String

    override fun toString(): String
}