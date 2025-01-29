package io.kotgres.dsl.queries.select

import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.select.base.SelectQuery


open class InnerSelectQuerySelect(query: SelectQuery) : FromQuerySelect(query) {

    /**
     * SELECT expression
     */

    fun andSelect(vararg names: String): FromQuerySelect {
        return FromQuerySelect(addFields(*names))
    }

    fun andSelect(vararg blocks: IQueryBlock): FromQuerySelect {
        return FromQuerySelect(addFields(*blocks))
    }

}
