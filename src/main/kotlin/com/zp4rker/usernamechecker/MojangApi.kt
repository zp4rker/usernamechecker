package com.zp4rker.usernamechecker

import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

fun searchName(username: String, at: Long = System.currentTimeMillis() / 1000): String? {
    val url = URL("https://api.mojang.com/users/profiles/minecraft/$username?at=$at")
    val con = url.openConnection()

    val input = con.getInputStream()
    val response = input.reader().readText()
    input.close()

    return if (response.isEmpty()) null else JSONObject(response).getString("id")
}

fun getProfile(uuid: String): Profile {
    val url = URL("https://api.mojang.com/user/profiles/$uuid/names")
    val con = url.openConnection()

    val input = con.getInputStream()
    val response = input.reader().readText()
    input.close()

    val raw = JSONArray(response)

    val profile = Profile(uuid)
    for (obj in raw) {
        if (obj !is JSONObject) continue
        val name = obj.getString("name")
        val updated = obj.optLong("changedToAt", 0)
        profile.usernames.add(Username(name, updated))
    }
    return profile
}

/*
* get original/first user of name
* get current user of name or null
* iterate users of name until same as current or returning null
* add each user to array
* */

fun getHistory(username: String): NameHistory {
    val history = NameHistory()

    var time: Long = 0
    var profile = getProfile(searchName(username, time) ?: return history).also { history.profiles.add(it) }

    while (true) {
        val index = profile.usernames.indexOfFirst { it.name.equals(username, true) && it.updated >= time }
        val temp = profile.usernames.getOrNull(index + 1) ?: break
        time = temp.updated / 1000 + 1

        profile = getProfile(searchName(username, time) ?: break).also { history.profiles.add(it) }
    }

    return history
}

data class NameHistory(val profiles: MutableList<Profile> = mutableListOf())
data class Profile(val uuid: String?, val usernames: MutableList<Username> = mutableListOf())
data class Username(val name: String, val updated: Long)