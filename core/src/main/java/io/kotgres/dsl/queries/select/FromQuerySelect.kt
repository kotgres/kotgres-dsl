package io.kotgres.dsl.queries.select

import io.kotgres.dsl.queries.select.base.SelectQuery


open class FromQuerySelect(query: SelectQuery) : SelectQuery(query) {

    /**
     * FROM from_item
     */

    fun from(table: String): JoinQuerySelect {
        tableFrom.add(table)
        return JoinQuerySelect(this)
    }

    fun from(table: String, asName: String): JoinQuerySelect {
        tableFrom.add("$table AS $asName")
        return JoinQuerySelect(this)
    }

    fun from(table: SelectQuery): JoinQuerySelect {
        tableFrom.add(table)
        return JoinQuerySelect(this)
    }

    fun from(table: SelectQuery, asName: String): JoinQuerySelect {
        tableFrom.add("$table AS $asName")
        return JoinQuerySelect(this)
    }

    fun from(tables: List<String>): JoinQuerySelect {
        tableFrom.addAll(tables)
        return JoinQuerySelect(this)
    }

}
