package io.kotgres.dsl.queries.select

import io.kotgres.dsl.queries.select.base.SelectQuery


open class JoinQuerySelect(query: SelectQuery) : WhereQuerySelect(query) {

    fun join(table: String): OnJoinQuerySelect {
        tableToJoin["join-@" + table] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun join(table: String, asName: String): OnJoinQuerySelect {
        tableToJoin["join-@**" + table + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun join(table: SelectQuery, asName: String): OnJoinQuerySelect {
        tableToJoin["join-@**" + table.toSql() + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun fullJoin(table: String): OnJoinQuerySelect {
        tableToJoin["full-@join-@" + table] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun fullJoin(table: SelectQuery, asName: String): OnJoinQuerySelect {
        tableToJoin["full-@join-@**" + table.toSql() + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }
    fun fullJoin(table: String, asName: String): OnJoinQuerySelect {
        tableToJoin["full-@join-@**" + table + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun leftJoin(table: String): OnJoinQuerySelect {
        tableToJoin["left-@join-@" + table] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun leftJoin(table: SelectQuery, asName: String): OnJoinQuerySelect {
        tableToJoin["left-@join-@**" + table.toSql() + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun leftJoin(table: String, asName: String): OnJoinQuerySelect {
        tableToJoin["left-@join-@**" + table + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun leftOuterJoin(table: String): OnJoinQuerySelect {
        tableToJoin["left-@outer-@join-@" + table] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun leftOuterJoin(table: SelectQuery, asName: String): OnJoinQuerySelect {
        tableToJoin["left-@outer-@join-@**" + table.toSql() + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun leftOuterJoin(table: String, asName: String): OnJoinQuerySelect {
        tableToJoin["left-@outer-@join-@**" + table + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun rightJoin(table: String): OnJoinQuerySelect {
        tableToJoin["right-@join-@" + table] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun rightJoin(table: SelectQuery, asName: String): OnJoinQuerySelect {
        tableToJoin["right-@join-@**" + table.toSql() + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun rightJoin(table: String, asName: String): OnJoinQuerySelect {
        tableToJoin["right-@join-@**" + table + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun rightOuterJoin(table: String): OnJoinQuerySelect {
        tableToJoin["right-@outer-@join-@" + table] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun rightOuterJoin(table: SelectQuery, asName: String): OnJoinQuerySelect {
        tableToJoin["right-@outer-@join-@**" + table.toSql() + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun rightOuterJoin(table: String, asName: String): OnJoinQuerySelect {
        tableToJoin["right-@outer-@join-@**" + table+ "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun innerJoin(table: String): OnJoinQuerySelect {
        tableToJoin["inner-@join-@" + table] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun innerJoin(table: SelectQuery, asName: String): OnJoinQuerySelect {
        tableToJoin["inner-@join-@**" + table.toSql() + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun innerJoin(table: String, asName: String): OnJoinQuerySelect {
        tableToJoin["inner-@join-@**" + table + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun crossJoin(table: String): OnJoinQuerySelect {
        tableToJoin["cross-@join-@" + table] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun crossJoin(table: String, asName: String): OnJoinQuerySelect {
        tableToJoin["cross-@join-@**" + table + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

    fun crossJoin(table: OnJoinQuerySelect, asName: String): OnJoinQuerySelect {
        tableToJoin["cross-@join-@**" + table.toSql() + "^^$asName"] = arrayListOf()
        return OnJoinQuerySelect(this)
    }

}
