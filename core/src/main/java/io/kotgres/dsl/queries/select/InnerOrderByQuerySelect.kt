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

    fun andOrderBy(vararg fields: String, order: Order = DEFAULT_ORDER, nulls: OrderByNulls? = null): InnerOrderByQuerySelect {
        return innerOrder(*fields, order = order, nulls = nulls)
    }

    fun andOrderBy(fields: List<String>, order: Order = DEFAULT_ORDER, nulls: OrderByNulls? = null): InnerOrderByQuerySelect {
        return andOrderBy(*fields.toTypedArray(), order = order, nulls = nulls)
    }

    fun andOrderByAsc(field: String, nulls: OrderByNulls? = null): InnerOrderByQuerySelect {
        return andOrderBy(field, order = Order.ASC, nulls = nulls)
    }

    fun andOrderByDesc(field: String, nulls: OrderByNulls? = null): InnerOrderByQuerySelect {
        return andOrderBy(field, order = Order.DESC, nulls = nulls)

    }

}
