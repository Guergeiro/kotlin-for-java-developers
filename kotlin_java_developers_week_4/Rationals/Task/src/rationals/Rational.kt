package rationals

import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

class Rational(numerator: BigInteger, denominator: BigInteger) : Comparable<Rational> {
    val numerator: BigInteger
    val denominator: BigInteger

    init {
        if (denominator == ZERO) {
            throw IllegalArgumentException()
        }

        this.numerator = numerator
        this.denominator = denominator
    }

    operator fun plus(other: Rational): Rational {
        val numerator =
            this.numerator * other.denominator +
                other.numerator * this.denominator
        val denominator = this.denominator * other.denominator
        return Rational(numerator, denominator)
    }

    operator fun minus(other: Rational): Rational {
        val numerator =
            this.numerator * other.denominator -
                other.numerator * this.denominator
        val denominator = this.denominator * other.denominator
        return Rational(numerator, denominator)
    }

    operator fun times(other: Rational): Rational {
        val numerator = this.numerator * other.numerator
        val denominator = this.denominator * other.denominator
        return Rational(numerator, denominator)
    }

    operator fun div(other: Rational): Rational {
        val numerator = this.numerator * other.denominator
        val denominator = this.denominator * other.numerator
        return Rational(numerator, denominator)
    }

    operator fun unaryMinus(): Rational {
        return Rational(-numerator, denominator)
    }

    override fun compareTo(other: Rational): Int {
        val a =
            Rational(
                this.numerator * other.denominator,
                this.denominator * other.denominator,
            )
        val b =
            Rational(
                other.numerator * this.denominator,
                other.denominator * this.denominator,
            )
        return (a.numerator).compareTo(b.numerator)
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Rational) {
            return false
        }

        val a = this.normalize()
        val b = other.normalize()

        return a.numerator == b.numerator && a.denominator == b.denominator
    }

    override fun toString(): String {
        return if (this.numerator % this.denominator == ZERO) {
            (this.numerator / this.denominator).toString()
        } else {
            val n = this.normalize()
            return "${n.numerator}/${n.denominator}"
        }
    }

    private fun normalize(): Rational {
        val gcd = this.numerator.gcd(this.denominator)
        return if (this.denominator < ZERO) {
            Rational(-numerator / gcd, -denominator / gcd)
        } else {
            Rational(numerator / gcd, denominator / gcd)
        }
    }
}

fun String.toRational(): Rational {
    val splitted = this.split("/")
    return when (splitted.size) {
        1 -> Rational(splitted.get(0).toBigInteger(), ONE)
        2 ->
            Rational(
                splitted.get(0).toBigInteger(),
                splitted.get(1).toBigInteger(),
            )
        else -> throw IllegalArgumentException()
    }
}

infix fun Number.divBy(other: Number): Rational {
    return Rational(
        BigInteger.valueOf(this.toLong()),
        BigInteger.valueOf(other.toLong()),
    )
}

infix fun BigInteger.divBy(other: BigInteger): Rational {
    return Rational(
        this,
        other,
    )
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println(
        "912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2,
    )
}
