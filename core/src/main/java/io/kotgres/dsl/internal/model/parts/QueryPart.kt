package io.kotgres.dsl.internal.model.parts

import io.kotgres.dsl.Order
import io.kotgres.dsl.OrderByNulls
import io.kotgres.dsl.internal.model.condition.base.Condition
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.common.QueryBuildingUtils
import io.kotgres.dsl.queries.select.base.SelectQuery
import java.util.EnumMap

// todo remove this class and move query building logic to SelectQuery
internal class QueryParts
    (
    var withAs: List<Pair<String, String>>,
    var selectQuery: SelectQuery,
    var fields: LinkedHashMap<String, Array<String>>,
    var from: List<Any>,
    var joins: LinkedHashMap<String, ArrayList<Condition>>,
    var where: List<Condition>,
    order: ArrayList<Triple<String, Order, OrderByNulls?>>,
    var limit: Int?,
    var offset: Int?,
    var groupBys: List<String>,
    havingCondition: List<Condition>,
    customExpression: EnumMap<CustomPart, List<IQueryBlock>>,
    val shouldLineBreak: Boolean
) {
    private val rgxFindField =
        "([^\\\"\\ ][a-zA-Z0-9\\_]+[^\\\"\\ ])\\.([^\\\"\\s][a-zA-Z0-9\\_\\-\\=\\`\\~\\:\\.\\,\\|\\*\\^\\#\\@\\\$])".toPattern()

    var statementSeparator: String
    var tabSeparator: String

    var havings: List<Condition> = havingCondition
    var order: List<Triple<String, Order, OrderByNulls?>> = order
    var custom: EnumMap<CustomPart, List<IQueryBlock>> = customExpression

    init {
        if (shouldLineBreak) {
            statementSeparator = "\n"
            tabSeparator = "\n  "
        } else {
            statementSeparator = " "
            tabSeparator = " "
        }
    }

    // TODO remove escaping? not needed for postgres
    private fun escapeTableName(statement: String, esp: String): String {
        val matcher = rgxFindField.matcher(statement)
        val rgxReplace = "\\$esp\$1\\$esp.\$2"
        return matcher.replaceAll(rgxReplace)
    }

    override fun toString(): String {
        return compile()
    }

    private fun buildFromPart(): String {
        if (from.isEmpty()) return ""

        var fromPart = ""
        val f = from[0]

        if (f is String) {
            val fromTables = if (from.size == 1) from else from.distinct()
            fromTables.forEach {
                val t = it.toString()
                if (t.isNotEmpty()) {
                    fromPart += "$t, "
                }
            }
            fromPart = fromPart.substring(0, fromPart.length - 2)
        } else if (f is SelectQuery) {
            val subParts = f.compile()
            if (subParts.parts.from.size == 2) {
                val subTable = subParts.parts.from[1]
                fromPart = "(${subParts.sql}) AS $subTable"
            } else {
                fromPart = "(${subParts.sql})"
            }
        }

        return "FROM $fromPart"
    }

    private fun buildJoinsPart(): String {
        val joinStatements = mutableListOf<String>()
        for ((join, conditions) in joins) {
            val parts = join.split("-@")
            val lastPart = parts.last()
            var asName = ""
            val tbl = if (lastPart.startsWith("**")) {
                asName = lastPart.substring(lastPart.indexOf("^^") + 2)
                val table = lastPart.removePrefix("**").removeSuffix("^^$asName")
                // a bit of a messy way to check whether it's a subquery or not and add parenthesis if it is
                if (table.startsWith("SELECT ")) {
                    "( $table )"
                } else {
                    table
                }
            } else {
                lastPart
            }

            val joinStmt = parts.subList(0, parts.size - 1).joinToString(" ").uppercase()
            var logicStmt = ""

            logicStmt += conditions.joinToString(" ") {
                "${it.getStatement()} ${it.getOperator()}"
            }

            if (logicStmt != "") {
                // remove last operator and spaces
                logicStmt = logicStmt.substring(0, logicStmt.length - 3).trim()
                val final = if (asName.isNotEmpty()) {
                    "$joinStmt $tbl AS $asName ON ( $logicStmt )"
                } else {
                    "$joinStmt $tbl ON ( $logicStmt )"
                }
                joinStatements.add(final)
            }
        }
        return joinStatements.joinToString(statementSeparator)
    }

    // WARNING: very similar code in UpdateQuery.buildWhere, if changing here also change there
    private fun buildWherePart(): String {
        return QueryBuildingUtils.buildWherePart(where, shouldLineBreak) ?: ""
    }


    private fun buildGroupByPart(): String {
        if (groupBys.isEmpty()) return ""

        val groupByPart = groupBys.joinToString(", ")

        return "GROUP BY $groupByPart"
    }

    private fun buildHavingPart(): String {
        if (havings.isEmpty()) return ""

        val lineBreak = if (shouldLineBreak) "\n  " else " "

        val wherePart = havings.mapIndexed { index, it ->
            val s = (if (index != 0) "${it.getOperator()} " else "") + it.getStatement()
            s.trimEnd()
        }.joinToString(lineBreak)

        return "HAVING$tabSeparator$wherePart"
    }

    private fun buildOrderByPart(): String {
        if (order.isEmpty()) return ""

        val orderPart = order.map {
            "${it.first} ${it.second}" + (if (it.third != null) " ${it.third}" else "")
        }.joinToString(", ")

        return "ORDER BY $orderPart"
    }

    private fun buildLimitOffsetPart(): String {
        val parts = mutableListOf<String>()
        if (limit != null) parts.add("LIMIT ${limit!!}")
        if (offset != null) parts.add("OFFSET $offset")
        return parts.joinToString(" ")
    }


    fun precompile(): Array<String> {
        val (columns, sqlFields) = selectQuery.compileSelect()

        val withAsPart = QueryBuildingUtils.buildWithAsPart(withAs, tabSeparator) ?: ""
        val fromPart = buildFromPart()
        val joinsPart = buildJoinsPart()
        val wherePart = buildWherePart()
        val groupByPart = buildGroupByPart()
        val havingPart = buildHavingPart()
        val orderPart = buildOrderByPart()
        val limitOffsetPart = buildLimitOffsetPart()

        return arrayOf(
            sqlFields,
            withAsPart,
            fromPart,
            joinsPart,
            wherePart,
            groupByPart,
            havingPart,
            orderPart,
            limitOffsetPart
        )
    }

    fun compile(): String {
        val allParts = precompile()
        val sqlFields = allParts[0]
        val withAsPart = allParts[1]
        val others = allParts.slice(2 until allParts.size)

        val customParts = CustomPart.entries.toTypedArray()
        val partNames = customParts.slice(2 until customParts.size)
        var parts = ""

        others.forEachIndexed { idx, s ->
            if (s != "") parts += "$statementSeparator$s"

            if (custom.containsKey(partNames[idx])) {
                val blocks = custom[partNames[idx]]
                blocks?.forEach {
                    parts += it.toString()
                }
            }
        }

        var sql = ""
        if (withAsPart != "") sql += "$withAsPart$statementSeparator"
        sql += "SELECT "
        if (custom.containsKey(CustomPart.SELECT)) {
            val blocks = custom[CustomPart.SELECT]
            blocks?.forEach {
                sql += "$it "
            }
        }

        sql += "$sqlFields "

        if (custom.containsKey(CustomPart.FIELD)) {
            val blocks = custom[CustomPart.SELECT]
            blocks?.forEach {
                sql += "$it "
            }
        }

        sql = sql.trimEnd()

        var result = sql
        if (parts != "") result += parts
        return result.trimEnd()
    }
}
