package io.kotgres.dsl.queries.update

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.update.base.UpdateQuery

open class WhereQueryUpdate internal constructor(query: UpdateQuery) : ReturningQueryUpdate(query) {

    fun where(vararg blocks: IQueryBlock): InnerWhereQueryUpdate {
        whereAnd(*blocks)
        return InnerWhereQueryUpdate(this)
    }

    fun where(vararg blocks: Raw): InnerWhereQueryUpdate {
        whereAnd(*blocks)
        return InnerWhereQueryUpdate(this)
    }

    fun where(vararg blocks: Any): InnerWhereQueryUpdate {
        whereAnd(*blocks)
        return InnerWhereQueryUpdate(this)
    }

}