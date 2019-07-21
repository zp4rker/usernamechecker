package com.zp4rker.usernamechecker

import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

fun username(username: String, at: Long = -1): JSONObject {
    val url = URL("https://api.mojang.com/users/profiles/minecraft/$username${if (at < 0) "" else "?at=$at"}")
    val con = url.openConnection()

    val input = con.getInputStream()
    val response = input.reader().readText()
    input.close()

    return JSONObject(response)
}

fun profile(uuid: String): JSONArray {
    val url = URL("https://api.mojang.com/user/profiles/$uuid/names")
    val con = url.openConnection()

    val input = con.getInputStream()
    val response = input.reader().readText()
    input.close()

    return JSONArray(response)
}

fun nameHistory(username: String)/*: JSONArray*/ {
    var current = username(username, 0)
    var profile = profile(current.getString("id"))
    while (!profile.getJSONObject(profile.length() - 1).getString("name").equals(username, true)) {
        println(profile)
        for ((index, obj) in profile.withIndex()) {
            if (obj !is JSONObject) continue
            if (!obj.getString("name").equals(username, true)) continue
            if (profile.length() == index + 1) break
            current = username(username, profile.getJSONObject(index + 1).getLong("changedToAt") + 1)
            profile = profile(current.getString("id"))
            break
        }
    }
    println(profile)
}