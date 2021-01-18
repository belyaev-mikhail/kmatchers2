package ru.spbstu.matchers.rewrite

sealed class Case<T1, T2, T3, T4,  T5, T6, in Arg> {
    abstract operator fun invoke(arg: Arg): MatchResult<T1, T2, T3, T4, T5, T6>?
}

operator fun <T1, T2, T3, T4, T5, T6, Arg>
        Case<T1, T2, T3, T4, T5, T6, Arg>.contains(arg: Arg): Boolean = this.invoke(arg) != null

internal class PatternCase<T1, T2, T3, T4, T5, T6, in Arg>(
    val pattern: Pattern<T1, T2, T3, T4, T5, T6, Arg>
): Case<T1, T2, T3, T4, T5, T6, Arg>() {
    override fun invoke(arg: Arg): MatchResult<T1, T2, T3, T4, T5, T6>? {
        val builder = MatchResult.Builder<T1, T2, T3, T4, T5, T6>()
        return when {
            pattern.unapply(arg, builder) -> builder
            else -> null
        }
    }
}

fun <T1, T2, T3, T4, T5, T6, Arg> case(pattern: Pattern<T1, T2, T3, T4, T5, T6, Arg>): Case<T1, T2, T3, T4, T5, T6, Arg> =
    PatternCase(pattern)

internal class OrCase<T1, T2, T3, T4, T5, T6, in Arg>(
    val elements: Collection<Case<T1, T2, T3, T4, T5, T6, Arg>>
): Case<T1, T2, T3, T4, T5, T6, Arg>() {
    constructor(vararg elements: Case<T1, T2, T3, T4, T5, T6, Arg>): this(elements.asList())

    override fun invoke(arg: Arg): MatchResult<T1, T2, T3, T4, T5, T6>? {
        for (e in elements) {
            return e(arg) ?: continue
        }
        return null
    }
}

infix fun <T1, T2, T3, T4, T5, T6, Arg> Case<T1, T2, T3, T4, T5, T6, Arg>.or(that: Case<T1, T2, T3, T4, T5, T6, Arg>): Case<T1, T2, T3, T4, T5, T6, Arg> =
    OrCase<T1, T2, T3, T4, T5, T6, Arg>(this, that)

internal class NotCase<T1, T2, T3, T4, T5, T6, in Arg>(
    val base: Case<T1, T2, T3, T4, T5, T6, Arg>
): Case<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Arg>() {

    override fun invoke(arg: Arg): MatchResult<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing>? =
        when (base(arg)) {
            null -> MatchResult.Builder()
            else -> null
        }
}

operator fun <T1, T2, T3, T4, T5, T6, Arg> Case<T1, T2, T3, T4, T5, T6, Arg>.not(): Case<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Arg> =
    NotCase(this)

internal class GuardedCase<T1, T2, T3, T4, T5, T6, in Arg>(
    val base: Case<T1, T2, T3, T4, T5, T6, Arg>,
    val guard: CaseBody<T1, T2, T3, T4, T5, T6, Boolean>
): Case<T1, T2, T3, T4, T5, T6, Arg>() {
    override fun invoke(arg: Arg): MatchResult<T1, T2, T3, T4, T5, T6>? = base(arg)?.takeIf { guard(it, it) }
}

infix fun <T1, T2, T3, T4, T5, T6, Arg>
        Case<T1, T2, T3, T4, T5, T6, Arg>.where(
    guard: CaseBody<T1, T2, T3, T4, T5, T6, Boolean>
): Case<T1, T2, T3, T4, T5, T6, Arg> = GuardedCase(this, guard)
