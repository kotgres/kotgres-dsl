package io.kotgres.dsl.queries.delete

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.delete.base.DeleteQuery

open class InnerWhereDeleteQuery internal constructor(query: DeleteQuery) : ReturningDeleteQuery(query) {

    /**
     * AND
     */

    fun andWhere(vararg blocks: IQueryBlock): InnerWhereDeleteQuery {
        whereAnd(*blocks)
        return InnerWhereDeleteQuery(this)
    }

    fun andWhere(vararg blocks: Raw): InnerWhereDeleteQuery {
        whereAnd(*blocks)
        return InnerWhereDeleteQuery(this)
    }

    fun andWhere(vararg blocks: Any): InnerWhereDeleteQuery {
        whereAnd(*blocks)
        return InnerWhereDeleteQuery(this)
    }

    /**
     * OR
     */

    fun orWhere(vararg blocks: IQueryBlock): InnerWhereDeleteQuery {
        whereOr(*blocks)
        return InnerWhereDeleteQuery(this)
    }

    fun orWhere(vararg blocks: Raw): InnerWhereDeleteQuery {
        whereOr(*blocks)
        return InnerWhereDeleteQuery(this)
    }

    fun orWhere(vararg blocks: Any): InnerWhereDeleteQuery {
        whereOr(*blocks)
        return InnerWhereDeleteQuery(this)
    }

}