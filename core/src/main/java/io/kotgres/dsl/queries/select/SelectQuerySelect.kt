package io.kotgres.dsl.queries.select

import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.select.base.SelectQuery


open class SelectQuerySelect(query: SelectQuery) : SelectQuery(query) {

    /**
     * SELECT expression
     */

    fun columns(columns: List<String>): InnerSelectQuerySelect {
        return InnerSelectQuerySelect(fields(columns))
    }

    fun select(vararg names: String): InnerSelectQuerySelect {
        return InnerSelectQuerySelect(fields(*names))
    }

    fun select(vararg blocks: IQueryBlock): InnerSelectQuerySelect {
        return InnerSelectQuerySelect(fields(*blocks))
    }

    fun select(vararg raw: Raw): InnerSelectQuerySelect {
        return InnerSelectQuerySelect(fields(*raw))
    }

    fun select(vararg blocks: Any): InnerSelectQuerySelect {
        return InnerSelectQuerySelect(fields(*blocks))
    }

}
