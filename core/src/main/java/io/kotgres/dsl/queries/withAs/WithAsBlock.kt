package io.kotgres.dsl.queries.withAs

import io.kotgres.dsl.Raw
import io.kotgres.dsl.queries.block.IQueryBlock
import io.kotgres.dsl.queries.delete.EmptyDeleteQuery
import io.kotgres.dsl.queries.delete.UsingDeleteQuery
import io.kotgres.dsl.queries.delete.base.DeleteQuery
import io.kotgres.dsl.queries.insert.ColumnsInsertQuery
import io.kotgres.dsl.queries.insert.InsertQueryEmpty
import io.kotgres.dsl.queries.insert.base.InsertQuery
import io.kotgres.dsl.queries.select.InnerSelectQuerySelect
import io.kotgres.dsl.queries.select.SelectQuerySelect
import io.kotgres.dsl.queries.select.base.SelectQuery
import io.kotgres.dsl.queries.update.EmptyUpdateQuery
import io.kotgres.dsl.queries.update.SetQueryUpdate
import io.kotgres.dsl.queries.update.base.UpdateQuery

class WithAsBlock(field: String, code: String) {

    private val withAs = mutableListOf<Pair<String, String>>()

    init {
        withAs.add(field to code)
    }

    fun andWithAs(field: String, code: SelectQuery): WithAsBlock {
        withAs.add(field to code.toString())
        return this
    }

    fun andWithAs(field: String, code: Raw): WithAsBlock {
        withAs.add(field to code.value)
        return this
    }

    /**
     * DUPLICATED AND MODIFIED FROM entrypoints.kt
     */

    /**
     * SELECT
     */

    fun select(vararg names: String): InnerSelectQuerySelect {
        return SelectQuerySelect(SelectQuery(withAs)).select(*names)
    }

    fun select(vararg block: IQueryBlock): InnerSelectQuerySelect {
        return SelectQuerySelect(SelectQuery(withAs)).select(*block)
    }

    fun select(vararg block: Any): InnerSelectQuerySelect {
        return SelectQuerySelect(SelectQuery(withAs)).select(*block)
    }

    fun select(vararg raw: Raw): InnerSelectQuerySelect {
        return SelectQuerySelect(SelectQuery(withAs)).select(*raw)
    }

    /**
     * INSERT
     */
    fun insertInto(tableName: String): ColumnsInsertQuery {
        return InsertQueryEmpty(InsertQuery(withAs)).insertInto(tableName)
    }

    /**
     * UPDATE
     */

    fun update(tableName: String): SetQueryUpdate {
        return EmptyUpdateQuery(UpdateQuery(withAs)).update(tableName)
    }


    /**
     * DELETE
     */

    fun deleteFrom(tableName: String): UsingDeleteQuery {
        return EmptyDeleteQuery(DeleteQuery(withAs)).deleteFrom(tableName)
    }

}