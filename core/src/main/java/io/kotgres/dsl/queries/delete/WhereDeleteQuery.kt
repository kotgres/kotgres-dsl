package io.kotgres.dsl.queries.delete

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.delete.base.DeleteQuery

open class WhereDeleteQuery internal constructor(query: DeleteQuery) : ReturningDeleteQuery(query) {

    fun where(vararg blocks: IQueryBlock): InnerWhereDeleteQuery {
        whereAnd(*blocks)
        return InnerWhereDeleteQuery(this)
    }

    fun where(vararg blocks: Raw): InnerWhereDeleteQuery {
        whereAnd(*blocks)
        return InnerWhereDeleteQuery(this)
    }

    fun where(vararg blocks: Any): InnerWhereDeleteQuery {
        whereAnd(*blocks)
        return InnerWhereDeleteQuery(this)
    }

}