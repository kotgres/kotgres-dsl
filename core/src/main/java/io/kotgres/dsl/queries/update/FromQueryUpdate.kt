package io.kotgres.dsl.queries.update

import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.update.base.UpdateQuery

open class FromQueryUpdate internal constructor(query: UpdateQuery) : WhereQueryUpdate(query) {

    fun from(expression: String): InnerFromQueryUpdate {
        fromBlocks.add(expression)
        return InnerFromQueryUpdate(this)
    }

    fun from(fromTable: IQueryBlock): WhereQueryUpdate {
        return from(fromTable.getStatement())
    }

}