package io.kotgres.dsl

enum class OrderByNulls(val value: String) {
    FIRST("FIRST"),
    LAST("LAST");

    override fun toString(): String {
        return "NULLS $value"
    }
}

val NULLS_FIRST = OrderByNulls.FIRST
val NULLS_LAST = OrderByNulls.LAST
