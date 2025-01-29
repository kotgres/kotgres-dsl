package io.kotgres.dsl.queries.select

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.select.base.SelectQuery

open class WhereQuerySelect(query: SelectQuery) : GroupByQuerySelect(query) {

    fun where(vararg blocks: IQueryBlock): InnerWhereQuerySelect {
        whereAnd(*blocks)
        return InnerWhereQuerySelect(this)
    }

    fun where(vararg blocks: Raw): InnerWhereQuerySelect {
        whereAnd(*blocks)
        return InnerWhereQuerySelect(this)
    }

    fun where(vararg blocks: Any): InnerWhereQuerySelect {
        whereAnd(*blocks)
        return InnerWhereQuerySelect(this)
    }

}
