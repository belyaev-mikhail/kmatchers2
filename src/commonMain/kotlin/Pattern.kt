package ru.spbstu.matchers.rewrite

import kotlinx.warnings.Warnings
import ru.spbstu.wheels.Option

fun interface Pattern<out T1, out T2, out T3, out T4, out T5, out T6, in Arg> {
    fun unapply(
        value: Arg,
        matchResult: MatchResult<@UV T1, @UV T2, @UV T3, @UV T4, @UV T5, @UV T6>
    ): Boolean
}

inline fun <
        T1, T2, T3, T4, T5, T6, Arg, Res
        > Pattern<T1, T2, T3, T4, T5, T6, Res>.contraMap(
    crossinline body: (Arg) -> Res
): Pattern<T1, T2, T3, T4, T5, T6, Arg> =
    Pattern { value, matchResult -> unapply(body(value), matchResult) }

inline fun <
        T1, T2, T3, T4, T5, T6, Res
        > Pattern<T1, T2, T3, T4, T5, T6, Res>.filter(
    crossinline body: (Res) -> Boolean
): Pattern<T1, T2, T3, T4, T5, T6, Res> =
    Pattern { value, matchResult -> body(value) && unapply(value, matchResult) }

inline fun <
        T1, T2, T3, T4, T5, T6, Arg1, Arg2, Res
        > Pattern<T1, T2, T3, T4, T5, T6, Arg1>.divideWith(
    that: Pattern<T1, T2, T3, T4, T5, T6, Arg2>,
    crossinline get1: (Res) -> Arg1,
    crossinline get2: (Res) -> Arg2
): Pattern<T1, T2, T3, T4, T5, T6, Res> =
    Pattern { value, matchResult ->
        val v1 = get1(value)
        val v2 = get2(value)
        this.unapply(v1, matchResult) && that.unapply(v2, matchResult)
    }

inline fun <
        T1, T2, T3, T4, T5, T6, Arg1, Arg2, Res
        > Pattern<T1, T2, T3, T4, T5, T6, Arg1>.divideWith(
    that: Pattern<T1, T2, T3, T4, T5, T6, Arg2>,
    crossinline body: (Res) -> Pair<Arg1, Arg2>
): Pattern<T1, T2, T3, T4, T5, T6, Res> = divideWith(that, { body(it).first }, { body(it).second })

fun any(): NoResultPattern<Any?> = NoResultPattern { _, _ -> true }

fun <T> _1(): Pattern<T, ε, ε, ε, ε, ε, T> = Pattern { value, matchResult ->
    matchResult as MatchResult.Builder
    when {
        matchResult.v1.isNotEmpty() -> {
            matchResult.v1.get() == value
        }
        else -> {
            matchResult.v1 = Option.just(value)
            true
        }
    }
}

fun <T> _2(): Pattern<ε, T, ε, ε, ε, ε, T> = Pattern { value, matchResult ->
    matchResult as MatchResult.Builder
    when {
        matchResult.v2.isNotEmpty() -> {
            matchResult.v2.get() == value
        }
        else -> {
            matchResult.v2 = Option.just(value)
            true
        }
    }
}

fun <T> _3(): Pattern<ε, ε, T, ε, ε, ε, T> = Pattern { value, matchResult ->
    matchResult as MatchResult.Builder
    when {
        matchResult.v3.isNotEmpty() -> {
            matchResult.v3.get() == value
        }
        else -> {
            matchResult.v3 = Option.just(value)
            true
        }
    }
}

fun <T> _4(): Pattern<ε, ε, ε, T, ε, ε, T> = Pattern { value, matchResult ->
    matchResult as MatchResult.Builder
    when {
        matchResult.v4.isNotEmpty() -> {
            matchResult.v4.get() == value
        }
        else -> {
            matchResult.v4 = Option.just(value)
            true
        }
    }
}

fun <T> _5(): Pattern<ε, ε, ε, ε, T, ε, T> = Pattern { value, matchResult ->
    matchResult as MatchResult.Builder
    when {
        matchResult.v5.isNotEmpty() -> {
            matchResult.v5.get() == value
        }
        else -> {
            matchResult.v5 = Option.just(value)
            true
        }
    }
}

fun <T> _6(): Pattern<ε, ε, ε, ε, ε, T, T> = Pattern { value, matchResult ->
    matchResult as MatchResult.Builder
    when {
        matchResult.v6.isNotEmpty() -> {
            matchResult.v6.get() == value
        }
        else -> {
            matchResult.v6 = Option.just(value)
            true
        }
    }
}

fun <T> const(arg: T): NoResultPattern<T> = Pattern { value, _ -> value == arg }

infix fun <T1, T2, T3, T4, T5, T6, Arg> Pattern<T1, T2, T3, T4, T5, T6, Arg>.and(that: Pattern<T1, T2, T3, T4, T5, T6, Arg>): Pattern<T1, T2, T3, T4, T5, T6, Arg> =
    Pattern { value, matchResult ->
        this.unapply(value, matchResult) && that.unapply(value, matchResult)
    }

fun interface OfType<T> : NoResultPattern<Any?> {
    fun check(value: Any?): Boolean
    override fun unapply(value: Any?, matchResult: MatchResult<ε, ε, ε, ε, ε, ε>): Boolean =
        check(value)
}

inline fun <reified T> ofType(): OfType<T> = OfType { it is T }

infix fun <T1, T2, T3, T4, T5, T6, Arg> OfType<Arg>.and(that: Pattern<T1, T2, T3, T4, T5, T6, Arg>): Pattern<T1, T2, T3, T4, T5, T6, Any?> =
    Pattern { value, matchResult ->
        @Suppress(Warnings.UNCHECKED_CAST)
        this.check(value) && that.unapply(value as Arg, matchResult)
    }

object NotNull : NoResultPattern<Any?> {
    override fun unapply(value: Any?, matchResult: EmptyResult): Boolean = value != null
}

fun notNull(): NotNull = NotNull
fun <T1, T2, T3, T4, T5, T6, Arg> notNull(
    inner: Pattern<T1, T2, T3, T4, T5, T6, Arg>
): Pattern<T1, T2, T3, T4, T5, T6, Arg?> = Pattern { value, matchResult ->
    value != null && inner.unapply(value, matchResult)
}

infix fun <T1, T2, T3, T4, T5, T6, Arg> NotNull.and(that: Pattern<T1, T2, T3, T4, T5, T6, Arg>): Pattern<T1, T2, T3, T4, T5, T6, Arg?> =
    Pattern { value, matchResult ->
        @Suppress(Warnings.UNCHECKED_CAST)
        value != null && that.unapply(value as Arg, matchResult)
    }
