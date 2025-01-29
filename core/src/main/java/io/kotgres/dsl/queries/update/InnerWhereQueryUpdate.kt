package io.kotgres.dsl.queries.update

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.update.base.UpdateQuery

class InnerWhereQueryUpdate internal constructor(query: UpdateQuery) : ReturningQueryUpdate(query) {

    /**
     * AND
     */

    fun andWhere(vararg blocks: IQueryBlock): InnerWhereQueryUpdate {
        whereAnd(*blocks)
        return InnerWhereQueryUpdate(this)
    }

    fun andWhere(vararg blocks: Raw): InnerWhereQueryUpdate {
        whereAnd(*blocks)
        return this
    }

    fun andWhere(vararg blocks: Any): InnerWhereQueryUpdate {
        whereAnd(*blocks)
        return this
    }

    /**
     * OR
     */

    fun orWhere(vararg blocks: IQueryBlock): InnerWhereQueryUpdate {
        whereOr(*blocks)
        return InnerWhereQueryUpdate(this)
    }

    fun orWhere(vararg blocks: Raw): InnerWhereQueryUpdate {
        whereOr(*blocks)
        return InnerWhereQueryUpdate(this)
    }

    fun orWhere(vararg blocks: Any): InnerWhereQueryUpdate {
        whereOr(*blocks)
        return InnerWhereQueryUpdate(this)
    }


}