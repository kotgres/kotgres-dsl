package io.kotgres.dsl.queries.select.base

import io.kotgres.dsl.Order
import io.kotgres.dsl.OrderByNulls
import io.kotgres.dsl.Raw
import io.kotgres.dsl.exceptions.DslUnexpectedQueryBlockTypeException
import io.kotgres.dsl.internal.model.condition.And
import io.kotgres.dsl.internal.model.condition.Or
import io.kotgres.dsl.internal.model.condition.base.Condition
import io.kotgres.dsl.internal.model.parts.CustomPart
import io.kotgres.dsl.internal.model.parts.QueryInfo
import io.kotgres.dsl.internal.model.parts.QueryParts
import io.kotgres.dsl.internal.model.parts.SelectPart
import io.kotgres.dsl.queries.base.ConditionalQuery
import io.kotgres.dsl.queries.base.IBaseQuery
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.common.QueryBuildingUtils
import io.kotgres.dsl.queries.select.InnerOrderByQuerySelect
import io.kotgres.dsl.queries.select.LimitQuerySelect
import java.util.*


open class SelectQuery internal constructor() : ConditionalQuery(), IBaseQuery {

    internal constructor(withAs: List<Pair<String, String>>) : this() {
        this.withAs.addAll(withAs)
    }

    protected constructor(query: SelectQuery) : this() {
        this.currentTable = query.currentTable
        this.tableFrom.addAll(query.tableFrom)
        this.withAs.addAll(query.withAs)
        query.fieldsToSelect.forEach { (k, v) ->
            this.fieldsToSelect[k] = v
        }
        query.tableToJoin.forEach { (k, v) ->
            this.tableToJoin[k] = v
        }
        this.whereCondition.addAll(query.whereCondition)
        this.groupBys.addAll(query.groupBys)
        this.havingCondition.addAll(query.havingCondition)
        this.orderBy.addAll(query.orderBy)
        query.expression.forEach { (k, v) ->
            this.expression[k] = v
        }
        this.rowLimit = query.rowLimit
        this.rowOffset = query.rowOffset
    }

    internal var asChar = ""

    protected var currentTable: String = ""

    protected val tableFrom = arrayListOf<Any>()

    protected val withAs = mutableListOf<Pair<String, String>>()

    private val fieldsToSelect by lazy {
        LinkedHashMap<String, Array<String>>()
    }

    internal val tableToJoin by lazy {
        LinkedHashMap<String, ArrayList<Condition>>()
    }

    protected val groupBys by lazy {
        arrayListOf<String>()
    }

    internal val havingCondition by lazy {
        arrayListOf<Condition>()
    }

    private val orderBy by lazy {
        arrayListOf<Triple<String, Order, OrderByNulls?>>()
    }

    internal val expression by lazy {
        EnumMap<CustomPart, List<IQueryBlock>>(CustomPart::class.java)
    }

    protected var rowLimit: Int? = null
    protected var rowOffset: Int? = null

    protected fun fields(vararg names: String): SelectQuery {
        fieldsToSelect[currentTable] = names as Array<String>
        return this
    }

    protected fun fields(names: List<String>): SelectQuery {
        fieldsToSelect[currentTable] = names.toTypedArray()
        return this
    }

    protected fun fields(vararg blocks: IQueryBlock): SelectQuery {
        fieldsToSelect[currentTable] = blocks.map { it.getStatement() }.toTypedArray()
        return this
    }

    // this allows mixing raw values and IQueryBlock in the same statement
    protected fun fields(vararg blocks: Any): SelectQuery {
        fieldsToSelect[currentTable] = QueryBuildingUtils.columnsToStringList("select", *blocks).toTypedArray()
        return this
    }

    private fun addToFieldsToSelect(toAdd: List<String>) {
        val afterAdd = fieldsToSelect[currentTable]!!.toMutableList().apply {
            this.addAll(toAdd)
        }

        fieldsToSelect[currentTable] = afterAdd.toTypedArray()
    }

    protected fun addFields(vararg names: String): SelectQuery {
        val toAdd = names.toList()
        addToFieldsToSelect(toAdd)
        return this
    }

    protected fun addFields(vararg blocks: IQueryBlock): SelectQuery {
        val toAdd = blocks.map { it.getStatement() }
        addToFieldsToSelect(toAdd)
        return this
    }

    internal fun compileSelect(): SelectPart {
        val selectFields = mutableListOf<String>()
        val columns = mutableListOf<String>()

        for ((tbl, cols) in fieldsToSelect) {
            for (colName in cols) {
                if (colName.indexOf("=") != -1) {
                    val parts = colName.split(asChar)
                    val partField = parts[0].trim()
                    val tblLinkedCol: String = partField

                    val selfCol = parts[1].trim()
                    if (tbl == "") {
                        selectFields.add(colName)
                    } else {
                        val aliasName = "$tbl-$selfCol"
                        columns.add(aliasName)
                        selectFields.add("$tblLinkedCol as $aliasName")
                    }
                } else {
                    if (tbl == "") {
                        selectFields.add(colName)
                    } else {
                        val aliasName = "$tbl-$colName"
                        columns.add(aliasName)
                        selectFields.add("$tbl.$colName as $aliasName")
                    }
                }
            }
        }

        val sqlFields = selectFields.joinToString(", ")
        return SelectPart(columns, sqlFields)
    }

    private fun toParts(shouldLineBreak: Boolean = false): QueryParts {
        val parts = QueryParts(
            withAs,
            this,
            fieldsToSelect,
            tableFrom,
            tableToJoin,
            whereCondition,
            orderBy,
            rowLimit,
            rowOffset,
            groupBys,
            havingCondition,
            expression,
            shouldLineBreak,
        )
        return parts
    }

    private fun addExpressionAfter(type: CustomPart, block: IQueryBlock) {
        if (this.expression.containsKey(type)) {
            (this.expression[type] as ArrayList).add(block)
        } else {
            this.expression[type] = arrayListOf(block)
        }
    }

    private fun compile(processor: (Array<String>) -> String, shouldLineBreak: Boolean = false): QueryInfo {
        val selectPart = compileSelect()
        val parts = toParts(shouldLineBreak)
        val partsArr = parts.precompile()
        val sql = processor(partsArr)
        return QueryInfo(sql, selectPart.columns, selectPart.sqlFields, parts)
    }

    internal fun compile(shouldLineBreak: Boolean = false): QueryInfo {
        val selectPart = compileSelect()
        val parts = toParts(shouldLineBreak)
        return QueryInfo(parts.compile(), selectPart.columns, selectPart.sqlFields, parts)
    }

    override fun toSql(shouldLineBreak: Boolean): String {
        return compile(shouldLineBreak).sql
    }

    override fun toString(): String {
        return compile().sql
    }

    /**
     * PROTECTED FUNCTIONS
     */
    protected fun innerOrder(
        vararg fields: String,
        order: Order,
        nulls: OrderByNulls? = null
    ): InnerOrderByQuerySelect {
        return InnerOrderByQuerySelect(order(listOf(*fields), order, nulls))
    }

    protected fun order(
        fields: List<String>,
        order: Order,
        nulls: OrderByNulls? = null
    ): LimitQuerySelect {
        for (field in fields) {
            orderBy.add(Triple(field, order, nulls))
        }
        return LimitQuerySelect(this)
    }

    protected fun addHaving(conditions: List<Any>, useOr: Boolean) {
        conditions.toList().forEach {
            val condition = when (it) {
                is String -> {
                    it
                }

                is IQueryBlock -> {
                    it.getStatement()
                }

                is Raw -> {
                    it.value
                }

                else -> {
                    throw DslUnexpectedQueryBlockTypeException("having")
                }
            }
            if (useOr) {
                havingCondition.add(Or(condition))
            } else {
                havingCondition.add(And(condition))
            }
        }
    }

    /**
     * PUBLIC FUNCTIONS
     */

    fun asTable(table: String): SelectQuery {
        tableFrom.add(table)
        return this
    }

}
