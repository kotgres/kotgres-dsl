package io.kotgres.dsl.queries.delete.base

import io.kotgres.dsl.queries.base.ConditionalQuery
import io.kotgres.dsl.queries.base.IDataManipulationQuery
import io.kotgres.dsl.queries.common.QueryBuildingUtils

open class DeleteQuery internal constructor() : ConditionalQuery(), IDataManipulationQuery {

    internal val withAs = mutableListOf<Pair<String, String>>()
    internal var toDeleteFromTable: String? = null
    internal var usingStatement: String? = null
    internal val returning: MutableList<String> = mutableListOf()

    constructor(withAs: List<Pair<String, String>>) : this() {
        this.withAs.addAll(withAs)
    }

    internal constructor(query: DeleteQuery) : this() {
        this.withAs.addAll(query.withAs)
        this.whereCondition.addAll(query.whereCondition)
        this.toDeleteFromTable = query.toDeleteFromTable
        this.usingStatement = query.usingStatement
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

        sql += "DELETE FROM $toDeleteFromTable"

        if (usingStatement != null) {
            sql += blockSeparator
            sql += "USING $usingStatement"
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