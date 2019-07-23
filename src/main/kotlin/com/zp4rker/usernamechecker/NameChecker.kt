package com.zp4rker.usernamechecker

fun main() {
    while (true) {
        print("Check availability of username: ")
        val input = readLine() ?: break
        if (input.isEmpty()) break

        println()
        println(checkAvailability(input))
        println()
        println(getHistory(input).profiles.reversed().joinToString("\n") { "${it.uuid} - ${it.usernames.last().name}" })
        println()
    }
}
