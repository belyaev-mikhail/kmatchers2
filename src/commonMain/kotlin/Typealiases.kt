package ru.spbstu.matchers.rewrite

internal typealias ε = Nothing
internal typealias NoResultPattern<Arg> = Pattern<ε, ε, ε, ε, ε, ε, Arg>
internal typealias EmptyResult = MatchResult<ε, ε, ε, ε, ε, ε>
internal typealias CaseBody<T1, T2, T3, T4, T5, T6, R> =
        MatchResult<T1, T2, T3, T4, T5, T6>.(MatchResult<T1, T2, T3, T4, T5, T6>) -> R

internal typealias UV = UnsafeVariance
