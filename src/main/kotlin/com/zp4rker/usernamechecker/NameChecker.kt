package com.zp4rker.usernamechecker

import org.jsoup.Jsoup
import java.net.URL

fun main() {
    nameHistory("woahchill")

    /*while (true) {
        val input = readLine() ?: break
        if (input.isEmpty()) break

        val username = search(input)
        println("${username.name} - ${username.status}")
    }*/
}

private fun search(username: String): Username {
    val url = URL("https://namemc.com/name/$username")
    val document = Jsoup.parse(url, 10000)
    val elements = document.select("#status-bar .col-sm-6")

    val status = when (val status = elements[0].getElementsByTag("div")[2].text().toLowerCase()) {
        "available", "available*" -> "\u001B[32m${status.dropLast(1)}"
        "available later" -> "\u001B[93m$status"
        "blocked" -> "\u001B[33m$status"
        else -> "\u001B[31m$status"
    } + "\u001B[0m"

    return Username(username, status)
}

data class Username(val name: String, val status: String)