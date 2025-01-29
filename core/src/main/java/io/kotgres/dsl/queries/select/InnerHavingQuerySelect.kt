package io.kotgres.dsl.queries.select

import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.select.base.SelectQuery


// TODO add tests and clean up this class' methods
open class InnerHavingQuerySelect(query: SelectQuery) : OrderByQuerySelect(query) {

    /**
     * AND HAVING condition
     */

    fun andHaving(vararg conditions: String): InnerHavingQuerySelect {
        addHaving(conditions.toList(), false)
        return InnerHavingQuerySelect(this)
    }

    fun andHaving(vararg conditions: Any): InnerHavingQuerySelect {
        addHaving(conditions.toList(), false)
        return InnerHavingQuerySelect(this)
    }


    fun andHaving(vararg conditions: IQueryBlock): InnerHavingQuerySelect {
        addHaving(conditions.toList(), false)
        return InnerHavingQuerySelect(this)
    }

    /**
     * OR HAVING condition
     */

    fun orHaving(vararg conditions: String): InnerHavingQuerySelect {
        addHaving(conditions.toList(), true)
        return InnerHavingQuerySelect(this)
    }

    fun orHaving(vararg conditions: Any): InnerHavingQuerySelect {
        addHaving(conditions.toList(), true)
        return InnerHavingQuerySelect(this)
    }


    fun orHaving(vararg conditions: IQueryBlock): InnerHavingQuerySelect {
        addHaving(conditions.toList(), true)
        return InnerHavingQuerySelect(this)
    }

}
