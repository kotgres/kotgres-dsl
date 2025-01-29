package io.kotgres.dsl.queries.select

import io.kotgres.dsl.queries.select.base.SelectQuery


open class GroupByQuerySelect(query: SelectQuery) : OrderByQuerySelect(query) {

    /**
     * GROUP BY element
     */
    fun groupBy(vararg fields: String): HavingQuerySelect {
        groupBys.addAll(fields.toList())
        return HavingQuerySelect(this)
    }

    fun groupBy(fields: List<String>): HavingQuerySelect {
        return groupBy(*fields.toTypedArray())
    }

}
