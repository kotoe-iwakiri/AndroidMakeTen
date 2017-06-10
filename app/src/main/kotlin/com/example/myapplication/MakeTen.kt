package com.example.myapplication

object MakeTen {

    val operators = listOf("+", "-", "*", "/")

    fun canMake(a: Int, b: Int, c: Int, d: Int): Boolean {
        val numPermutation = listOf(a, b, c, d).permutations().toList()
        val orderPermutation = listOf(1, 2, 3).permutations().toList()
        val operatorPermutation = operatorPermutation
        val results =
                numPermutation.flatMap { nums ->
                    orderPermutation.flatMap { orders ->
                        operatorPermutation.map { op ->
                            eval(nums, orders, op)
                        }
                    }
                }
        return results.contains(Rational(10, 1))
    }

    fun <T : Any> List<T>.permutations(): Sequence<List<T>> = if (size == 1) sequenceOf(this) else {
        val iterator = iterator()
        var head = iterator.next()
        var permutations = (this - head).permutations().iterator()

        fun nextPermutation(): List<T>? = if (permutations.hasNext()) listOf(head) + permutations.next() else {
            if (iterator.hasNext()) {
                head = iterator.next()
                permutations = (this - head).permutations().iterator()
                nextPermutation()
            } else null
        }

        generateSequence { nextPermutation() }
    }

    val operatorPermutation: List<List<String>> =
            operators.flatMap { op1 ->
                operators.flatMap { op2 ->
                    operators.map { op3 ->
                        listOf(op1, op2, op3)
                    }
                }
            }

    fun eval(nums: List<Int>, orders: List<Int>, ops: List<String>): Rational? = solveRpn(makeRpn(nums, orders, ops))

    fun makeRpn(nums: List<Int>, orders: List<Int>, ops: List<String>): List<String> =
            (orders zip ops).fold(nums.map { n -> n.toString() }) { acc, (order, op) ->
                val i = index(acc, { s: String -> s.matches(Regex("""\d""")) }, order)
                if (acc.drop(i).any { it.isOperator() })
                    acc.insert(op, acc.indexOfLast { it.isOperator() } + 1)
                else
                    acc.insert(op, i + 1)
            }

    /**
     * pを満たすa番目の要素(zero-origin)のindex
     * e.g. index(Seq(0,1,2,3,4,5,6,7,8,9), i: Int => i % 2 == 0, 2))
     *  -> 4
     */
    fun <A> index(xs: List<A>, p: (A) -> Boolean, a: Int): Int {
        tailrec fun <B> f(xs: List<B>, p: (B) -> Boolean, a: Int, b: Int): Int {
            require(a >= 0)
            val i = xs.indexOfFirst(p)
            return if (a == 0) b + i else f(xs.drop(i + 1), p, a - 1, b + i + 1)
        }
        return f(xs, p, a, 0)
    }

    fun String.isOperator(): Boolean = this in operators

    fun <T> List<T>.insert(a: T, i: Int): List<T> {
        val before = this.subList(0, i)
        val after = this.subList(i, this.size)
        return before + a + after
    }

    fun solveRpn(strings: List<String>): Rational? {
        fun foldingFunc(stack: List<String>, str: String): List<String> {
            return if (stack.size >= 2) {
                val x = stack.first()
                val y = stack.drop(1).first()
                val ys = stack.drop(2)
                when (str) {
                    "+" -> listOf((Rational.parse(y) + Rational.parse(x)).toString()) + ys
                    "-" -> listOf((Rational.parse(y) - Rational.parse(x)).toString()) + ys
                    "*" -> listOf((Rational.parse(y) * Rational.parse(x)).toString()) + ys
                    "/" -> listOf((Rational.parse(y) / Rational.parse(x)).toString()) + ys
                    else -> listOf(str) + stack
                }
            } else listOf(str) + stack
        }

        return try {
            strings.fold(listOf<String>()) { acc, s -> foldingFunc(acc, s) }.first()
        } catch(e: Exception) {
            null
        }?.let { Rational.parse(it) }
    }


    class Rational(n: Int, d: Int) {
        init {
            require(d != 0)
        }

        private fun gcd(x: Int, y: Int): Int =
                if (x == 0) y
                else if (x < 0) gcd(-x, y)
                else if (y < 0) -gcd(x, -y)
                else gcd(y % x, x)

        private val g = gcd(n, d)

        val numer: Int = n / g
        val denom: Int = d / g

        operator fun plus(that: Rational): Rational = Rational(numer * that.denom + that.numer * denom, denom * that.denom)

        operator fun minus(that: Rational): Rational = Rational(numer * that.denom - that.numer * denom, denom * that.denom)

        operator fun times(that: Rational): Rational = Rational(numer * that.numer, denom * that.denom)

        operator fun div(that: Rational): Rational = Rational(numer * that.denom, denom * that.numer)

        override fun toString(): String = "${numer}/${denom}"

        override fun equals(other: Any?): Boolean =
                if (other is Rational) numer == other.numer && denom == other.denom
                else false

        override fun hashCode(): Int = 31 * numer + denom

        companion object {
            val rationalPattern: Regex = Regex("""(-?\d+)/(-?\d+)""")
            val intPattern: Regex = Regex("""-?\d+""")

            fun parse(s: String): Rational =
                    (rationalPattern.matchEntire(s)?.destructured?.let {
                        val (n, d) = it
                        Rational(n.toInt(), d.toInt())
                    } ?: if (intPattern.matches(s)) Rational(s.toInt(), 1) else null)!!
        }
    }


}
