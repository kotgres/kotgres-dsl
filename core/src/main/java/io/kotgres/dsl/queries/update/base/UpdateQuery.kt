package io.kotgres.dsl.queries.update.base

import io.kotgres.dsl.queries.base.ConditionalQuery
import io.kotgres.dsl.queries.base.IDataManipulationQuery
import io.kotgres.dsl.queries.common.QueryBuildingUtils

open class UpdateQuery internal constructor() : ConditionalQuery(), IDataManipulationQuery {

    internal val withAs = mutableListOf<Pair<String, String>>()
    internal val toUpdateFields: MutableList<Pair<String, String>> = mutableListOf()
    internal var toUpdateTable: String? = null
    internal val returning: MutableList<String> = mutableListOf()
    internal val fromBlocks: MutableList<String> = mutableListOf()

    internal constructor(withAs: List<Pair<String, String>>) : this() {
        this.withAs.addAll(withAs)
    }

    internal constructor(query: UpdateQuery) : this() {
        this.withAs.addAll(query.withAs)
        this.whereCondition.addAll(query.whereCondition)
        this.toUpdateFields.addAll(query.toUpdateFields)
        this.toUpdateTable = query.toUpdateTable
        this.returning.addAll(query.returning)
    }


    override fun toSql(shouldLineBreak: Boolean): String {
        val blockSeparator = if (shouldLineBreak) "\n" else " "
        val tabSeparator = if (shouldLineBreak) "\n  " else " "

        val withAsPart = QueryBuildingUtils.buildWithAsPart(withAs, tabSeparator)

        var sql = ""

        if (withAsPart != null) {
            sql += "$withAsPart$blockSeparator"
        }

        sql += "UPDATE $toUpdateTable"

        if (toUpdateFields.isNotEmpty()) {
            sql += "${blockSeparator}SET "
            val setStatements = toUpdateFields.map { field ->
                "${field.first}=${field.second}"
            }

            sql += if (shouldLineBreak) {
                setStatements.joinToString(",\n  ")
            } else {
                setStatements.joinToString(", ")
            }
        }

        val wherePart = buildWhere(shouldLineBreak)
        if (wherePart != null) {
            sql += blockSeparator
            sql += wherePart
        }

        sql += QueryBuildingUtils.buildReturning(returning, shouldLineBreak)

        return sql.trimEnd()
    }

    override fun toString(): String {
        return toSql(false)
    }

    override fun hasReturning(): Boolean {
        return returning.isNotEmpty()
    }

}