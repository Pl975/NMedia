package ru.netology.nmedia.util

object NMediaUtils {
    private const val letters: String = "KM"
    fun numFormat(num: Int): String {
        if (num == 0) return ""
        val numAsString = num.toString()
        val index = (numAsString.length - 1) / 3 - 1
        when {
            index < 0 -> return numAsString
            index >= letters.length -> throw IllegalArgumentException("Number too large")
        }
        val letter = letters[index]
        val pos = numAsString.length - 3 * (index + 1)
        val begin = numAsString.substring(0, pos)
        val decimal = when {
            begin.length > 1 || numAsString[pos] == '0' -> ""
            else -> "." + numAsString[pos]
        }
        return begin + decimal + letter
    }
    const val AUTHOR = "Me"
    const val PUBLISHED = "Now"
}