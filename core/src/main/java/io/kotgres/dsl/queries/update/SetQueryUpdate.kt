package io.kotgres.dsl.queries.update

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.select.base.SelectQuery
import io.kotgres.dsl.queries.update.base.UpdateQuery

class SetQueryUpdate internal constructor(query: UpdateQuery) : UpdateQuery(query) {

    fun set(field: String, value: String): InnerSetQueryUpdate {
        toUpdateFields.add(field to "'${value}'")
        return InnerSetQueryUpdate(this)
    }

    fun set(field: String, value: Int): InnerSetQueryUpdate {
        toUpdateFields.add(field to value.toString())
        return InnerSetQueryUpdate(this)
    }

    fun set(field: String, value: Long): InnerSetQueryUpdate {
        toUpdateFields.add(field to value.toString())
        return InnerSetQueryUpdate(this)
    }

    fun set(field: String, value: Double): InnerSetQueryUpdate {
        toUpdateFields.add(field to value.toString())
        return InnerSetQueryUpdate(this)
    }

    fun set(field: String, raw: Raw): InnerSetQueryUpdate {
        toUpdateFields.add(field to raw.value)
        return InnerSetQueryUpdate(this)
    }

    fun set(field: String, block: IQueryBlock): InnerSetQueryUpdate {
        toUpdateFields.add(field to block.getStatement())
        return InnerSetQueryUpdate(this)
    }

    fun set(field: String, value: SelectQuery): InnerSetQueryUpdate {
        toUpdateFields.add(field to "( $value )")
        return InnerSetQueryUpdate(this)
    }

    fun set(field: String, value: Any): InnerSetQueryUpdate {
        toUpdateFields.add(field to value.toString())
        return InnerSetQueryUpdate(this)
    }

    fun setList(pairList: List<Pair<String, String>>): FromQueryUpdate {
        toUpdateFields.addAll(pairList)
        return InnerSetQueryUpdate(this)
    }

}