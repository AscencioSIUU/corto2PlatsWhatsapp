package com.example.privatechatplats.data

fun cesarCipher(input: String, shift: Int): String {
    val result = StringBuilder()

    for (char in input) {
        if (char.isLetter()) {
            val base = if (char.isUpperCase()) 'A' else 'a'
            val shiftedChar = ((char - base + shift) % 26 + base.code).toChar()
            result.append(shiftedChar)
        } else {
            result.append(char)
        }
    }

    return result.toString()
}