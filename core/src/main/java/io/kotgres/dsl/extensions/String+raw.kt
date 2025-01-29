package io.kotgres.dsl.extensions

import io.kotgres.dsl.Raw

fun String.raw(): Raw = Raw(this)

val String.raw: Raw
    get() = Raw(this)
