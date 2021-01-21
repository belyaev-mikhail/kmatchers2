package ru.spbstu.matchers

import ru.spbstu.matchers.rewrite.*
import ru.spbstu.wheels.Option
import kotlin.test.Test
import kotlin.test.assertEquals

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
    fun complexTest() {
        assertEquals(Var("x"), fullySimplify(Plus(Var("x"), Const(0))))
        assertEquals(Var("x"), fullySimplify(Plus(Plus(Const(10), Const(-10)), Plus(Var("x"), Const(0)))))
    }

    @Test
    fun views() {
        val intString = Pattern.viewOrFail { it: String -> Option.ofNullable(it.toIntOrNull()) }
        val positive = Pattern.simple<Int> { it > 0 }
        match("1") with {
            case(intString(positive)) of {

            }
        }
    }

    @Test
    fun regex() {
        val intString = Pattern.viewOrFail { it: String -> Option.ofNullable(it.toIntOrNull()) }
        val assignment = Pattern.regex("(\\d+) = (\\d+)")
        val three = match("1 = 2") with {
            case(assignment(intString(_1()), intString(_2()))) of {
                _1() + _2()
            }
            otherwise { -1 }
        }

        assertEquals(3, three)
    }



    fun honourableMentions() {
        match("Hello") with {
            // does not compile (error message could be better though)
            //case(_1() and _2()) of {}

            // does not compile with: type mismatch, required Nothing, found String
            // case(_1()) or case(_2()) of {}

            otherwise {  }
        }
    }
}