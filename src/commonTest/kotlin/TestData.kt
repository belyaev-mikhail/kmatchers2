package ru.spbstu.matchers

import ru.spbstu.matchers.rewrite.Pattern
import ru.spbstu.matchers.rewrite.any

sealed class Expr
data class Const(val value: Int) : Expr()
data class Var(val name: String) : Expr()
data class Plus(val lhv: Expr, val rhv: Expr) : Expr()

fun <T1, T2, T3, T4, T5, T6> Const(
    value: Pattern<T1, T2, T3, T4, T5, T6, Int> = any()
): Pattern<T1, T2, T3, T4, T5, T6, Expr> = Pattern { v, matchResult ->
    v is Const && value.unapply(v.value, matchResult)
}

fun <T1, T2, T3, T4, T5, T6> Var(
    name: Pattern<T1, T2, T3, T4, T5, T6, String> = any()
): Pattern<T1, T2, T3, T4, T5, T6, Expr> = Pattern { value, matchResult ->
    value is Var && name.unapply(value.name, matchResult)
}

fun <T1, T2, T3, T4, T5, T6> Plus(
    lhv: Pattern<T1, T2, T3, T4, T5, T6, Expr> = any(),
    rhv: Pattern<T1, T2, T3, T4, T5, T6, Expr> = any()
): Pattern<T1, T2, T3, T4, T5, T6, Expr> = Pattern { value, matchResult ->
    value is Plus &&
            lhv.unapply(value.lhv, matchResult) &&
            rhv.unapply(value.rhv, matchResult)
}
