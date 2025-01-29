package io.kotgres.dsl.queries.base

interface IDataManipulationQuery : IBaseQuery {
    fun hasReturning(): Boolean
}