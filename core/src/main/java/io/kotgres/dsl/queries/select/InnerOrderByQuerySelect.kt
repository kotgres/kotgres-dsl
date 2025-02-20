package io.kotgres.dsl.queries.select

import io.kotgres.dsl.internal.Constants.DEFAULT_ORDER
import io.kotgres.dsl.OrderByNulls
import io.kotgres.dsl.Order
import io.kotgres.dsl.queries.select.base.SelectQuery


open class InnerOrderByQuerySelect(query: SelectQuery) : LimitQuerySelect(query) {

    /**
     * ORDER BY expression
     */

    fun andOrderBy(field: String, order: Order = DEFAULT_ORDER, nulls: OrderByNulls? = null): InnerOrderByQuerySelect {
        return innerOrder(field, order = order, nulls = nulls)
    }

    fun andOrderBy(fields: List<String>, order: Order = DEFAULT_ORDER, nulls: OrderByNulls? = null): InnerOrderByQuerySelect {
        return innerOrder(*fields.toTypedArray(), order = order, nulls = nulls)
    }

}
