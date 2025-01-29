package io.kotgres.dsl.queries.select

import io.kotgres.dsl.exceptions.DslException
import io.kotgres.dsl.internal.model.condition.And
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.select.base.SelectQuery


open class OnJoinQuerySelect(query: SelectQuery) : JoinQuerySelect(query) {

    fun on(joinCondition: IQueryBlock): InnerOnJoinQuerySelect {
        return on(joinCondition.toString())
    }

    fun on(joinConditionList: List<String>): InnerOnJoinQuerySelect {
        if (tableToJoin.isEmpty()) throw DslException("tableToJoin not found")

        val tblName = tableToJoin.entries.last().key
        for (joinCondition in joinConditionList) {
            tableToJoin[tblName]?.add(And(joinCondition))
        }

        return InnerOnJoinQuerySelect(this)
    }

    fun on(joinCondition: String): InnerOnJoinQuerySelect {
        if (tableToJoin.isEmpty()) throw DslException("tableToJoin not found")

        val tblName = tableToJoin.entries.last().key
        tableToJoin[tblName]?.add(And(joinCondition))

        return InnerOnJoinQuerySelect(this)
    }

}
