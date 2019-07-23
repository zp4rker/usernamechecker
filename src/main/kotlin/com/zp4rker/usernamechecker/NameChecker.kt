package com.zp4rker.usernamechecker

fun main() {
    getHistory("woahchill").profiles.forEach { println(it.usernames.last()) }
}
