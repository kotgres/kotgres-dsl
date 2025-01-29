package io.kotgres.dsl.queries.insert.base

import io.kotgres.dsl.ConflictSet
import io.kotgres.dsl.internal.model.condition.base.Condition
import io.kotgres.dsl.queries.base.IDataManipulationQuery
import io.kotgres.dsl.queries.common.QueryBuildingUtils

internal enum class OverrideType(val sqlString: String) {
    SYSTEM("SYSTEM"),
    USER("USER");
}

open class InsertQuery : IDataManipulationQuery {

    internal val withAs = mutableListOf<Pair<String, String>>()
    internal val toInsertColumns: MutableList<String> = mutableListOf()
    internal val toInsertValues: MutableList<List<Any>> = mutableListOf()
    internal var isDefaultValues = false
    internal var toInsertTable: String? = null
    internal val returning: MutableList<String> = mutableListOf()
    internal var conflictTarget: String? = null
    internal var conflictResolutionSet: Boolean = false
    internal var conflictSetList: List<ConflictSet>? = null
    internal var overrideType: OverrideType? = null
    internal val onConflictDoUpdateWhereConditions by lazy {
        arrayListOf<Condition>()
    }

    constructor(withAs: List<Pair<String, String>>) {
        this.withAs.addAll(withAs)
    }

    internal constructor(query: InsertQuery) {
        this.withAs.addAll(query.withAs)
        this.toInsertColumns.addAll(query.toInsertColumns)
        this.toInsertValues.addAll(query.toInsertValues)
        this.isDefaultValues = query.isDefaultValues
        this.toInsertTable = query.toInsertTable
        this.returning.addAll(query.returning)
        this.conflictTarget = query.conflictTarget
        this.conflictResolutionSet = query.conflictResolutionSet
        this.conflictSetList = query.conflictSetList
        this.overrideType = query.overrideType
        this.onConflictDoUpdateWhereConditions.addAll(query.onConflictDoUpdateWhereConditions)
    }

    internal constructor()

    override fun toSql(shouldLineBreak: Boolean): String {
        val blockSeparator = if (shouldLineBreak) "\n" else " "
        val tabSeparator = if (shouldLineBreak) "\n  " else " "

        val withAsPart = QueryBuildingUtils.buildWithAsPart(withAs, tabSeparator)

        var sql = ""

        if (withAsPart != null) {
            sql += "$withAsPart$blockSeparator"
        }

        sql += "INSERT INTO $toInsertTable"

        if (toInsertColumns.isNotEmpty()) {
            sql += " ( " + toInsertColumns.joinToString(", ") + " )"
        }

        if (overrideType != null) {
            sql += blockSeparator
            sql += "OVERRIDING ${overrideType!!.sqlString} VALUE"
        }

        if (isDefaultValues) {
            sql += blockSeparator
            sql += "DEFAULT VALUES"
        } else {
            if (toInsertValues.isNotEmpty()) {
                sql += blockSeparator

                val valuesString = toInsertValues.map {
                    "( " + it.joinToString(", ") + " )"
                }.joinToString(", ")

                sql += "VALUES $valuesString"
            }
        }

        if (conflictTarget != null) {
            sql += blockSeparator
            sql += "ON CONFLICT $conflictTarget"
            if (conflictResolutionSet) {
                if (conflictSetList != null) {
                    val lineBreakWithTab = if (shouldLineBreak) "\n  " else " "
                    val setExpresions = conflictSetList!!.joinToString(", ") {
                        "${it.columnName} = ${QueryBuildingUtils.valueToString(it.setExpression)}"
                    }
                    sql += " DO UPDATE${lineBreakWithTab}SET $setExpresions"

                    sql += buildOnConflictDoUpdateWhereConditions(shouldLineBreak)
                } else {
                    sql += " DO NOTHING"
                }
            }
        }

        if (returning.isNotEmpty()) {
            sql += QueryBuildingUtils.buildReturning(returning, shouldLineBreak)
        }

        return sql.trimEnd()
    }

    override fun toString(): String {
        return toSql(false)
    }

    override fun hasReturning(): Boolean {
        return returning.isNotEmpty()
    }

    /**
     * PRIVATE METHODS
     */

    private fun buildOnConflictDoUpdateWhereConditions(shouldLineBreak: Boolean): String {
        if (onConflictDoUpdateWhereConditions.isEmpty()) return ""

        val lineBreak = if (shouldLineBreak) "\n  " else " "

        val wherePart = onConflictDoUpdateWhereConditions.mapIndexed { index, it ->
            val s = (if (index != 0) "${it.getOperator()} " else "") + it.getStatement()
            s.trimEnd()
        }.joinToString(lineBreak)

        // todo remove this replace
        return (if (shouldLineBreak) "\n" else " ") + "WHERE" + lineBreak + wherePart
    }
}