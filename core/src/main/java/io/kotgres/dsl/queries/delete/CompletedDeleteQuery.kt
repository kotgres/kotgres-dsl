package io.kotgres.dsl.queries.delete

import io.kotgres.dsl.queries.delete.base.DeleteQuery

open class CompletedDeleteQuery internal constructor(query: DeleteQuery) : DeleteQuery(query)