package ru.spbstu.matchers

import ru.spbstu.matchers.rewrite.*
import kotlin.test.Test

import ru.spbstu.matchers.rewrite.Iterable
import kotlin.test.assertEquals

sealed class Expr
data class Const(val value: Int) : Expr()
data class Var(val name: String) : Expr()
data class Plus(val lhv: Expr, val rhv: Expr) : Expr()

fun <T1, T2, T3, T4, T5, T6> Const(
    value: Pattern<T1, T2, T3, T4, T5, T6, Int>
): Pattern<T1, T2, T3, T4, T5, T6, Expr> = Pattern { v, matchResult ->
    v is Const && value.unapply(v.value, matchResult)
}

fun <T1, T2, T3, T4, T5, T6> Var(
    name: Pattern<T1, T2, T3, T4, T5, T6, String>
): Pattern<T1, T2, T3, T4, T5, T6, Expr> = Pattern { value, matchResult ->
    value is Var && name.unapply(value.name, matchResult)
}

fun <T1, T2, T3, T4, T5, T6> Plus(
    lhv: Pattern<T1, T2, T3, T4, T5, T6, Expr>,
    rhv: Pattern<T1, T2, T3, T4, T5, T6, Expr>
): Pattern<T1, T2, T3, T4, T5, T6, Expr> = Pattern { value, matchResult ->
    value is Plus &&
            lhv.unapply(value.lhv, matchResult) &&
            rhv.unapply(value.rhv, matchResult)
}

fun simplify(e: Expr): Expr = match(e) with {
    case(Plus(Const(_1()), Const(_2()))) of {
        // _1() is Int, _2() is Int
        Const(_1() + _2())
    }
    case(Plus(Const(_2()), _1())) or case(Plus(_1(), Const(_2()))) where { _2() == 0 } of {
        // _1() is Expr, _2() is Int
        _1()
    }
    case(Plus(_1(), _2())) of {
        // _1() is Expr, _2() is Expr
        Plus(simplify(_1()), simplify(_2()))
    }
    otherwise { e }
}

fun join(c: Iterable<Expr>) = match(c) with {
    case(Iterable(_1(), _1(), _1(), rest = _2())) of { _1() }

}

fun fullySimplify(e: Expr): Expr {
    var result = e
    do {
        val oldResult = result
        result = simplify(result)
    } while (oldResult !== result)
    return result
}

class SmokeTest {
    @Test
    fun smokeyTest() {
        assertEquals(Var("x"), fullySimplify(Plus(Var("x"), Const(0))))
        assertEquals(Var("x"), fullySimplify(Plus(Plus(Const(10), Const(-10)), Plus(Var("x"), Const(0)))))
    }
}