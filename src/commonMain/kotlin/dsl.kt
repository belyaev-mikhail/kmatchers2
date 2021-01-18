package ru.spbstu.matchers.rewrite

import ru.spbstu.wheels.Option
import kotlin.experimental.ExperimentalTypeInference

@DslMarker
annotation class MatchersDSL

@MatchersDSL
class MatchScope<T>(@PublishedApi internal val value: T) {
    @MatchersDSL
    object FakeReceiver

    @MatchersDSL
    inner class MatchWithScope<R> {
        @PublishedApi
        internal var result: Option<R> = Option.empty()

        // bug triggers IAE if directly referenced in inline function
        fun getValue(): T = this@MatchScope.value

        fun <T1, T2, T3, T4, T5, T6> case(pattern: Pattern<T1, T2, T3, T4, T5, T6, T>): Case<T1, T2, T3, T4, T5, T6, T> =
            PatternCase(pattern)

        inline infix fun <T1, T2, T3, T4, T5, T6>
                Case<T1, T2, T3, T4, T5, T6, T>.of(body: CaseBody<T1, T2, T3, T4, T5, T6, R>) {
            if (result.isNotEmpty()) return

            val res = this(getValue()) ?: return
            result = Option.just(body(res, res))
        }

        inline fun otherwise(body: () -> R) {
            if (result.isNotEmpty()) return

            result = Option.just(body())
        }
    }

    @OptIn(ExperimentalTypeInference::class)
    infix fun <R> with(@BuilderInference body: MatchWithScope<R>.() -> Unit): R =
        MatchWithScope<R>().apply(body).result.get()
}

@OptIn(ExperimentalTypeInference::class)
fun <T> match(value: T) = MatchScope(value)


