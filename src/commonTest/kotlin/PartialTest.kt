package ru.spbstu.matchers

import ru.spbstu.matchers.rewrite._1
import ru.spbstu.matchers.rewrite.match
import kotlin.test.Test
import kotlin.test.assertEquals

class PartialTest {
    @Test
    fun testNamed() {
        val data = Plus(Var("x"), Plus(Const(30), Var("y")))

        val res = match(data) with {
            case(Plus(rhv = Plus(lhv = Const(_1())))) of {
                _1()
            }
            otherwise { 80 }
        }

        assertEquals(30, res)

    }
}