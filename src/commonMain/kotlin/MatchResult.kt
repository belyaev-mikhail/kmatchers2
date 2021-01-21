package ru.spbstu.matchers.rewrite

import ru.spbstu.wheels.Option
import ru.spbstu.wheels.getOrElse

@MatchersDSL
sealed class MatchResult<out T1, out T2, out T3, out T4, out T5, out T6> {
    abstract fun _1(): T1
    abstract fun _2(): T2
    abstract fun _3(): T3
    abstract fun _4(): T4
    abstract fun _5(): T5
    abstract fun _6(): T6

    internal class Builder<T1, T2, T3, T4, T5, T6>: MatchResult<T1, T2, T3, T4, T5, T6>() {
        var v1: Option<T1> = Option.empty()
        var v2: Option<T2> = Option.empty()
        var v3: Option<T3> = Option.empty()
        var v4: Option<T4> = Option.empty()
        var v5: Option<T5> = Option.empty()
        var v6: Option<T6> = Option.empty()

        private fun throwNoElement(index: Int): Nothing =
            throw NoSuchElementException("Component $index did not match anything")

        override fun _1(): T1 = v1.getOrElse { throwNoElement(1) }
        override fun _2(): T2 = v2.getOrElse { throwNoElement(2) }
        override fun _3(): T3 = v3.getOrElse { throwNoElement(3) }
        override fun _4(): T4 = v4.getOrElse { throwNoElement(4) }
        override fun _5(): T5 = v5.getOrElse { throwNoElement(5) }
        override fun _6(): T6 = v6.getOrElse { throwNoElement(6) }

        private fun <T> StringBuilder.appendElement(element: Option<T>): StringBuilder {
            if (element.isEmpty()) append("<NO VALUE>")
            else append(element.get())
            return this
        }

        override fun toString(): String = buildString {
            val realSize = when {
                v6.isNotEmpty() -> 6
                v5.isNotEmpty() -> 5
                v4.isNotEmpty() -> 4
                v3.isNotEmpty() -> 3
                v2.isNotEmpty() -> 2
                v1.isNotEmpty() -> 1
                else -> 0
            }
            append("[")
            if (realSize >= 1) appendElement(v1)
            if (realSize >= 2) append(", ").appendElement(v2)
            if (realSize >= 3) append(", ").appendElement(v3)
            if (realSize >= 4) append(", ").appendElement(v4)
            if (realSize >= 5) append(", ").appendElement(v5)
            if (realSize >= 6) append(", ").appendElement(v6)
            append("]")
        }
    }
}

operator fun <T1, T2, T3, T4, T5, T6> MatchResult<T1, T2, T3, T4, T5, T6>.component1(): T1 = _1()
operator fun <T1, T2, T3, T4, T5, T6> MatchResult<T1, T2, T3, T4, T5, T6>.component3(): T3 = _3()
operator fun <T1, T2, T3, T4, T5, T6> MatchResult<T1, T2, T3, T4, T5, T6>.component2(): T2 = _2()
operator fun <T1, T2, T3, T4, T5, T6> MatchResult<T1, T2, T3, T4, T5, T6>.component5(): T5 = _5()
operator fun <T1, T2, T3, T4, T5, T6> MatchResult<T1, T2, T3, T4, T5, T6>.component4(): T4 = _4()
operator fun <T1, T2, T3, T4, T5, T6> MatchResult<T1, T2, T3, T4, T5, T6>.component6(): T6 = _6()

