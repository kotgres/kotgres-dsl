package io.kotgres.dsl.queries.common

import io.kotgres.dsl.Raw
import io.kotgres.dsl.exceptions.DslUnexpectedQueryBlockTypeException
import io.kotgres.dsl.internal.model.condition.base.Condition
import io.kotgres.dsl.queries.block.IQueryBlock

internal object QueryBuildingUtils {

    fun buildWithAsPart(
        withAs: List<Pair<String, String>>,
        tabSeparator: String
    ): String? {
        if (withAs.isEmpty()) return null

        val withAsPart = withAs.mapIndexed { index, it ->
            "${it.first} AS ( ${it.second} )"
        }.joinToString(",${tabSeparator}")

        // no line break since it's potentially the first statement
        return "WITH${tabSeparator}" + withAsPart
    }

    fun buildReturning(returning: List<String>, shouldLineBreak: Boolean): String {
        if (returning.isEmpty()) return ""

        val lineBreak = if (shouldLineBreak) "\n  " else " "

        val wherePart = returning.mapIndexed { index, it ->
            it.trimEnd()
        }.joinToString(", ")

        return (if (shouldLineBreak) "\n" else " ") + "RETURNING" + lineBreak + wherePart
    }

    fun columnsToStringList(type: String, vararg blocks: Any): List<String> {
        return blocks.map {
            when (it) {
                is IQueryBlock -> it.getStatement()
                is String -> it
                is Raw -> it.value
                else -> throw DslUnexpectedQueryBlockTypeException(type)
            }
        }
    }

    fun valuesToStringList(type: String, vararg blocks: Any): List<String> {
        return blocks.map {
            valueToString(it)
        }
    }

    fun valueToString(value: Any?): String {
        if (value == null) return "null"

        return when (value) {
            is String -> "'$value'"
            is Int -> value.toString()
            is Double -> value.toString()
            is Long -> value.toString()
            is Raw -> value.value
            is IQueryBlock -> value.getStatement()
            else -> value.toString()
        }
    }

    fun buildWherePart(whereCondition: List<Condition>, shouldLineBreak: Boolean): String? {
        if (whereCondition.isEmpty()) return null

        val lineBreak = if (shouldLineBreak) "\n  " else " "

        val wherePart = whereCondition.mapIndexed { index, it ->
            val s = (if (index != 0) "${it.getOperator()} " else "") + it.getStatement()
            s.trimEnd()
        }.joinToString(lineBreak)

        return "WHERE$lineBreak$wherePart"
    }
}