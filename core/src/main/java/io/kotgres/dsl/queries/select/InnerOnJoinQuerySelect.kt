package io.kotgres.dsl.queries.select

import io.kotgres.dsl.exceptions.DslException
import io.kotgres.dsl.internal.model.condition.And
import io.kotgres.dsl.internal.model.condition.Or
import io.kotgres.dsl.queries.select.base.SelectQuery


open class InnerOnJoinQuerySelect(query: SelectQuery) : JoinQuerySelect(query) {

    fun andOn(joinCondition: String): InnerOnJoinQuerySelect {
        if (tableToJoin.isEmpty()) throw DslException("tableToJoin not found")

        val tblName = tableToJoin.entries.last().key
        tableToJoin[tblName]?.add(And(joinCondition))

        return this
    }

    fun orOn(joinCondition: String): InnerOnJoinQuerySelect {
        if (tableToJoin.isEmpty()) throw DslException("tableToJoin not found")

        val tblName = tableToJoin.entries.last().key
        tableToJoin[tblName]?.add(Or(joinCondition))

        return this
    }

}
