package io.kotgres.dsl.queries.select

import io.kotgres.dsl.internal.Constants.DEFAULT_ORDER
import io.kotgres.dsl.OrderByNulls
import io.kotgres.dsl.Order
import io.kotgres.dsl.queries.select.base.SelectQuery


open class OrderByQuerySelect(query: SelectQuery) : LimitQuerySelect(query) {

    /**
     * ORDER BY expression
     */

    fun orderBy(field: String, order: Order = DEFAULT_ORDER, nulls: OrderByNulls? = null): InnerOrderByQuerySelect {
        return innerOrder(field, order = order, nulls = nulls)
    }

    fun orderBy(vararg fields: String, order: Order = DEFAULT_ORDER, nulls: OrderByNulls? = null): InnerOrderByQuerySelect {
        return innerOrder(*fields, order = order, nulls = nulls)
    }

    fun orderBy(fields: List<String>, order: Order = DEFAULT_ORDER, nulls: OrderByNulls? = null): InnerOrderByQuerySelect {
        return orderBy(*fields.toTypedArray(), order = order, nulls = nulls)
    }

    fun orderByAsc(field: String, nulls: OrderByNulls? = null): InnerOrderByQuerySelect {
        return orderBy(field, order = Order.ASC, nulls = nulls)
    }

    fun orderByDesc(field: String, nulls: OrderByNulls? = null): InnerOrderByQuerySelect {
        return orderBy(field, order = Order.DESC, nulls = nulls)
    }

}
