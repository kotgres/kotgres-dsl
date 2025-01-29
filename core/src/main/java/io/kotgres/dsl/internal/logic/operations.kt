package io.kotgres.dsl.internal.logic

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.block.QueryBlock
import io.kotgres.dsl.queries.select.base.SelectQuery

internal object Operations {
    /**
     * EQUALITY
     */
    fun eq(field: String, value: Any): IQueryBlock {
        val processedValue = valueToSql(value)
        return QueryBlock(field, "=", processedValue)
    }

    /**
     * INEQUALITY
     */
    fun neq(field: String, value: Any): IQueryBlock {
        val processedValue = valueToSql(value)
        return QueryBlock(field, "!=", processedValue)
    }

    /**
     * GREATER
     */
    fun greater(field: String, value: Any): IQueryBlock {
        val processedValue = valueToSql(value)
        return QueryBlock(field, ">", processedValue)
    }

    /**
     * GREATER OR EQUAL
     */
    fun greaterEq(field: String, value: Any): IQueryBlock {
        val processedValue = valueToSql(value)
        return QueryBlock(field, ">=", processedValue)
    }


    /**
     * LESS
     */
    fun less(field: String, value: Any): IQueryBlock {
        val processedValue = valueToSql(value)
        return QueryBlock(field, "<", processedValue)
    }


    /**
     * LESS OR EQUAL
     */
    fun lessEq(field: String, value: Any): IQueryBlock {
        val processedValue = valueToSql(value)
        return QueryBlock(field, "<=", processedValue)
    }


    /**
     * LIKE
     */

    fun like(field: String, value: Any): IQueryBlock {
        val processedValue = valueToSql(value)
        return QueryBlock(field, "LIKE", processedValue)
    }

    fun notLike(field: String, value: Any): IQueryBlock {
        val processedValue = valueToSql(value)
        return QueryBlock(field, "NOT LIKE", processedValue)
    }

    fun ilike(field: String, value: Any): IQueryBlock {
        val processedValue = valueToSql(value)
        return QueryBlock(field, "ILIKE", processedValue)
    }

    fun notIlike(field: String, value: Any): IQueryBlock {
        val processedValue = valueToSql(value)
        return QueryBlock(field, "NOT ILIKE", processedValue)
    }

    /**
     * REGEXP
     */

    fun regexp(field: String, regex: String): IQueryBlock {
        return QueryBlock("REGEX_LIKE( ", field, ", '$regex' )")
    }

    fun regexpRaw(field: String, raw: Raw): IQueryBlock {
        return QueryBlock("REGEX_LIKE( ", field, ", '${raw.value}' )")
    }

//    fun regexp(field: String, regex: String, regexOption: String): IQueryBlock {
//        return TriQueryBlock("REGEX_LIKE( ", field, ", '$regex', '$regexOption' )")
//    }

    fun notRegexp(field: String, regex: String): IQueryBlock {
        return QueryBlock("NOT REGEX_LIKE( ", field, ", '$regex' )")
    }

    fun notRegexpRaw(field: String, raw: Raw): IQueryBlock {
        return QueryBlock("NOT REGEX_LIKE( ", field, ", '${raw.value}' )")
    }

//    fun notRegexp(field: String, regex: String, regexOption: String): IQueryBlock {
//        return TriQueryBlock("NOT REGEX_LIKE( ", field, ", '$regex', '$regexOption' )")
//    }


    /**
     * BETWEEN
     */
    fun <T> between(field: String, values: Pair<T, T>): IQueryBlock {
        return when (val left = values.first) {
            is String -> {
                val right = values.second.toString()
                QueryBlock(
                    "$field BETWEEN",
                    computeAnyValue(left),
                    "AND ${computeAnyValue(right)}"
                )
            }

            is Int -> {
                val right = values.second as Int
                return buildBetween(field, left, right)
            }

            is Long -> {
                val right = values.second as Long
                return buildBetween(field, left, right)
            }

            is Double -> {
                val right = values.second as Double
                return buildBetween(field, left, right)
            }

            else -> {
                val right = values.second
                return buildBetween(field, left.toString(), right.toString())
            }
        }
    }

    private fun buildBetween(field: String, left: Any, right: Any): IQueryBlock {
        return QueryBlock("$field BETWEEN $left AND $right")
    }

    /**
     * FULL TEXT SEARCH
     */
    fun match(field: String, search: String): IQueryBlock {
        return QueryBlock(field, "@@", computeAnyValue(search))
    }

    fun match(field: String, raw: Raw): IQueryBlock {
        return QueryBlock(field, "@@", raw.value)
    }

    /**
     * NULLABILITY
     */
    internal fun isNull(field: String): IQueryBlock {
        return QueryBlock(field, "IS NULL")
    }

    internal fun isNotNull(field: String): IQueryBlock {
        return QueryBlock(field, "IS NOT NULL")
    }


    /**
     * IN LIST
     */
    internal fun inList(stmt: String, values: List<Any>): IQueryBlock {
        val valEsp = inListToSqlList(values)
        return QueryBlock(stmt, "IN", "($valEsp)")
    }

    internal fun inList(stmt: String, values: Array<*>): IQueryBlock {
        val valEsp = inListToSqlList(values.toList())
        return QueryBlock(stmt, "IN", "($valEsp)")
    }

    internal fun inList(field: String, raw: Raw): IQueryBlock {
        return QueryBlock(field, "IN", "(${raw.value})")
    }

    internal fun inListBindings(field: String, bindingsCount: Int): IQueryBlock {
        val bindings = List(bindingsCount) { "?" }.joinToString(", ")
        return QueryBlock(field, "IN", "($bindings)")
    }

    internal fun notInList(stmt: String, values: List<Any>): IQueryBlock {
        val valEsp = inListToSqlList(values)
        return QueryBlock(stmt, "NOT IN", "($valEsp)")
    }

    internal fun notInList(stmt: String, values: Array<*>): IQueryBlock {
        val valEsp = inListToSqlList(values.toList())
        return QueryBlock(stmt, "NOT IN", "($valEsp)")
    }

    internal fun notInList(field: String, raw: Raw): IQueryBlock {
        return QueryBlock(field, "NOT IN", "(${raw.value})")
    }

    internal fun notInListBindings(field: String, bindingsCount: Int): IQueryBlock {
        val bindings = List(bindingsCount) { "?" }.joinToString(", ")
        return QueryBlock(field, "NOT IN", "($bindings)")
    }

    private fun inListToSqlList(values: List<*>): String {
        return listToSqlArray(values)
    }


    /**
     * ANY OPERATOR
     * TODO write tests
     */

    internal fun eqAny(field: String, values: List<Any>): IQueryBlock {
        val valEsp = anyListToSqlList(values)
        return QueryBlock(field, "=", "ANY($valEsp)")
    }

    internal fun eqAny(field: String, raw: Raw): IQueryBlock {
        return QueryBlock(field, "=", "ANY(${raw.value})")
    }

    internal fun neqAny(field: String, values: List<Any>): IQueryBlock {
        val valEsp = anyListToSqlList(values)
        return QueryBlock(field, "!=", "ANY($valEsp)")
    }

    internal fun neqAny(field: String, values: Array<*>): IQueryBlock {
        val valEsp = anyListToSqlList(values.toList())
        return QueryBlock(field, "!=", "ANY($valEsp)")
    }

    internal fun neqAny(field: String, raw: Raw): IQueryBlock {
        return QueryBlock(field, "!=", "ANY(${raw.value})")
    }

    private fun anyListToSqlList(values: List<*>): String {
        return listToSqlArray(values)
    }

    /**
     * UTILS
     */
    private fun computeAnyValue(value: Any): String {
        return "'${value.toString().replace("'", "''")}'"
    }

    private fun valueToSql(value: Any): String {
        return when (value) {
            // Native
            is Double -> value.toString()
            is Int -> value.toString()
            is Long -> value.toString()
            is Boolean -> value.toString()
            is String -> computeAnyValue(value) // custom case due to bindings
            // Dsl
            is Raw -> value.value
            is IQueryBlock -> "( $value )"
            is SelectQuery -> "( $value )"
            // Default
            else -> computeAnyValue(value)
        }
    }

    private fun listToSqlArray(values: List<*>) = values.map {
        if (it is String) {
            "'${it.replace("'", "''")}'"
        } else {
            it
        }
    }.joinToString(", ")

}