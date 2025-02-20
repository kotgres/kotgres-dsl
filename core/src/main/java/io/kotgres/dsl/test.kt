package io.kotgres.dsl

import io.kotgres.dsl.select
import io.kotgres.dsl.operators.*

fun main() {
    val sql = select("*")
        .from("user")
        .where("id" eq 1)
        .orderBy("id")
        .limit(100)
        .toSql(true)
    println(sql)
}