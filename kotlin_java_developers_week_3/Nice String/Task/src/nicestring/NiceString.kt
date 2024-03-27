package nicestring

fun String.isNice(): Boolean {
    // 1 Check if doesn't contain substrings
    val noSubstrings =
        listOf("bu", "ba", "be").none {
            it in this
        }
    // 2 Check if at least 3 vowels are present
    val hasVowels =
        "aeiou".sumOf { ch ->
            this.count { ch == it }
        } >= 3
    // 3 Check if contains two consecutive letters
	val firstString = this.drop(1)
	val secondString= this.dropLast(1)
	val zipped = firstString.zip(secondString)
	val twoConsecutive = zipped.any { (a, b) -> a == b }

    // 4 Check if at least two of them are true
    return listOf(noSubstrings, hasVowels, twoConsecutive).count { it == true } > 1
}
