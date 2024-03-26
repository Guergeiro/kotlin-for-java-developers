package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(
    secret: String,
    guess: String,
): Evaluation {
    val available =
        secret.fold(
            mutableMapOf<Char, Int>(),
            fun (
                prev: MutableMap<Char, Int>,
                current: Char,
            ): MutableMap<Char, Int> {
                val value = prev.getOrDefault(current, 0)
                prev.put(current, value + 1)
                return prev
            },
        )

    val rightPosition = available.countGuessIf(guess) { index, c -> secret.get(index) == c }
    val wrongPosition = available.countGuessIf(guess) { index, c -> secret.get(index) != c }
    return Evaluation(
        wrongPosition = wrongPosition,
        rightPosition = rightPosition,
    )
}

fun MutableMap<Char, Int>.countGuessIf(
    guess: String,
    ifFun: (index: Int, c: Char) -> Boolean,
): Int {
    var count = 0
    for ((index, c) in guess.withIndex()) {
        val existing = this.getOrDefault(c, 0)
        if (existing < 1) {
            continue
        }

        if (ifFun(index, c)) {
            count += 1
            this.put(c, existing - 1)
        }
    }
    return count
}

fun evaluateGuessSolution(
    secret: String,
    guess: String,
): Evaluation {
    val rightPositions = secret.zip(guess).count { (a, b) -> a == b }

    val commonLetters =
        "ABCDEF".sumOf { ch ->

            Math.min(secret.count { it == ch }, guess.count { it == ch })
        }
    return Evaluation(rightPositions, commonLetters - rightPositions)
}
