package io.kotgres.dsl

enum class Order(private val sqlValue: String) {
    ASC("ASC"),
    DESC("DESC");

    override fun toString(): String {
        return sqlValue
    }
}

val ASC = Order.ASC
val DESC = Order.DESC
