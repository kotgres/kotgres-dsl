package io.kotgres.dsl.queries.update

import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.update.base.UpdateQuery

class InnerFromQueryUpdate internal constructor(query: UpdateQuery) : WhereQueryUpdate(query) {

    fun andFrom(expression: String): InnerFromQueryUpdate {
        fromBlocks.add(expression)
        return this
    }

    fun andFrom(queryBlock: IQueryBlock): InnerFromQueryUpdate {
        return andFrom(queryBlock.getStatement())
    }

}