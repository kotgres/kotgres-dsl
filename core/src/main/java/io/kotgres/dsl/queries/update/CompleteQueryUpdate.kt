package io.kotgres.dsl.queries.update

import io.kotgres.dsl.queries.update.base.UpdateQuery

open class CompleteQueryUpdate internal constructor(query: UpdateQuery) : UpdateQuery(query)