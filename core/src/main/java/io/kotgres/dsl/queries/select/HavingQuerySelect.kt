package io.kotgres.dsl.queries.select

import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.select.base.SelectQuery


// TODO add tests and clean up this class' methods
open class HavingQuerySelect(query: SelectQuery) : OrderByQuerySelect(query) {

    /**
     * HAVING condition
     */

    fun having(vararg conditions: String): InnerHavingQuerySelect {
        addHaving(conditions.toList(), false)
        return InnerHavingQuerySelect(this)
    }

    fun having(vararg conditions: Any): InnerHavingQuerySelect {
        addHaving(conditions.toList(), false)
        return InnerHavingQuerySelect(this)
    }


    fun having(vararg conditions: IQueryBlock): InnerHavingQuerySelect {
        addHaving(conditions.toList(), false)
        return InnerHavingQuerySelect(this)
    }

}
