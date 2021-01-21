package ru.spbstu.matchers

import ru.spbstu.matchers.rewrite.*
import kotlin.test.Test
import kotlin.test.assertEquals

class NegativeTest {
    @Test
    fun testNegatives() {
        val data = Plus(Var("x"), Plus(Var("y"), Plus(Const(0), Const(1))))
        val res = match(data) with {
            case(Var(_1())) of {
                1
            }
            case(Plus(Plus(any(), _2()), _1())) of {
                2
            }
            case(Plus(rhv = Plus(Var(_1()), Plus(Var(_2()))))) of {
                3
            }
            otherwise { 4 }
        }
        assertEquals(4, res)
    }

    @Test
    fun testGuards() {
        val data = Plus(Var("x"), Plus(Var("y"), Plus(Const(0), Const(1))))
        val res = match(data) with {
            case(Var(_1())) of {
                1
            }
            case(Plus(Plus(any(), _2()), _1())) of {
                2
            }
            case(Plus(rhv = Plus(Var(_1()), Plus(rhv = Const(_2()))))) where { _2() > 2 } of {
                3
            }
            otherwise { 4 }
        }
        assertEquals(4, res)
    }
}