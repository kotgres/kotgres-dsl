package io.kotgres.dsl.queries.select

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.select.base.SelectQuery


open class InnerWhereQuerySelect(query: SelectQuery) : GroupByQuerySelect(query) {

    /**
     * WHERE INNER
     */

    /**
     * AND
     */


    fun andWhere(vararg blocks: IQueryBlock): InnerWhereQuerySelect {
        whereAnd(*blocks)
        return InnerWhereQuerySelect(this)
    }

    fun andWhere(vararg blocks: Raw): InnerWhereQuerySelect {
        whereAnd(*blocks)
        return InnerWhereQuerySelect(this)
    }


    fun andWhere(vararg blocks: Any): InnerWhereQuerySelect {
        whereAnd(*blocks)
        return InnerWhereQuerySelect(this)
    }

    /**
     * OR
     */

    fun orWhere(vararg blocks: IQueryBlock): InnerWhereQuerySelect {
        whereOr(*blocks)
        return InnerWhereQuerySelect(this)
    }

    fun orWhere(vararg blocks: Raw): InnerWhereQuerySelect {
        whereOr(*blocks)
        return InnerWhereQuerySelect(this)
    }

    fun orWhere(vararg blocks: Any): InnerWhereQuerySelect {
        whereOr(*blocks)
        return InnerWhereQuerySelect(this)
    }

}
