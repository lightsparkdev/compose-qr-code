#!/usr/bin/env kotlin
import java.io.File
import kotlin.system.exitProcess

/**
 * A script to run locally in order to make a release.
 *
 * Inspired by https://github.com/apollographql/apollo-kotlin/blob/ef8fa4c222bc34cb00c4294699e324e9604a4b81/scripts/tag.main.kts
 */


if (runCommand("git", "status", "--porcelain").isNotEmpty()) {
    println("Your git repo is not clean. Make sur to stash or commit your changes before making a release")
    exitProcess(1)
}

check(args.size == 1) {
    "versions.main.kts [version to tag]"
}
val tagVersion = args[0]
val nextSnapshot = getNextSnapshot(tagVersion)


while (true) {
    println("Current version is '${getCurrentVersion()}'.")
    println("Tag '$tagVersion' and bump to $nextSnapshot [y/n]?")

    when (readLine()!!.trim()) {
        "y" -> break
        "n" -> {
            println("Aborting.")
            exitProcess(1)
        }
    }
}

setCurrentVersion(tagVersion)
setVersionInDocs(tagVersion, nextSnapshot)

runCommand("git", "commit", "-a", "-m", "release $tagVersion")
runCommand("git", "tag", "v$tagVersion")

setCurrentVersion(nextSnapshot)
runCommand("git", "commit", "-a", "-m", "version is now $nextSnapshot")

println("Everything is done. Verify everything is ok and push upstream to trigger the new version.")

fun runCommand(vararg args: String): String {
    val builder = ProcessBuilder(*args)
        .redirectError(ProcessBuilder.Redirect.INHERIT)

    val process = builder.start()
    val ret = process.waitFor()

    val output = process.inputStream.bufferedReader().readText()
    if (ret != 0) {
        throw java.lang.Exception("command ${args.joinToString(" ")} failed:\n$output")
    }

    return output
}

fun setCurrentVersion(version: String) {
    print("Setting version in gradle props")
    val gradleProperties = File("composeqrcode/gradle.properties")
    val newContent = gradleProperties.readLines().joinToString(separator = "\n", postfix = "\n") {
        it.replace(Regex("VERSION_NAME=.*"), "VERSION_NAME=$version")
    }
    gradleProperties.writeText(newContent)
}

fun getCurrentVersion(): String {
    val versionLines = File("composeqrcode/gradle.properties").readLines().filter { it.startsWith("VERSION_NAME=") }

    require(versionLines.isNotEmpty()) {
        "cannot find the version in ./gradle.properties"
    }

    require(versionLines.size == 1) {
        "multiple versions found in ./gradle.properties"
    }

    val regex = Regex("VERSION_NAME=(.*)-SNAPSHOT")
    val matchResult = regex.matchEntire(versionLines.first())

    require(matchResult != null) {
        "'${versionLines.first()}' doesn't match VERSION_NAME=(.*)-SNAPSHOT"
    }

    return matchResult.groupValues[1] + "-SNAPSHOT"
}

fun getNextSnapshot(version: String): String {
    val components = version.split(".").toMutableList()
    val part = components.removeLast()
    var digitCount = 0
    for (i in part.indices.reversed()) {
        if (part[i] < '0' || part[i] > '9') {
            break
        }
        digitCount++
    }

    check(digitCount > 0) {
        "Cannot find a number to bump in $version"
    }

    // prefix can be "alpha", "dev", etc...
    val prefix = if (digitCount < part.length) {
        part.substring(0, part.length - digitCount)
    } else {
        ""
    }
    val numericPart = part.substring(part.length - digitCount, part.length)
    val asNumber = numericPart.toInt()

    val nextPart = if (numericPart[0] == '0') {
        // https://docs.gradle.org/current/userguide/single_versions.html#version_ordering
        // Gradle understands that alpha2 > alpha10 but it might not be the case for everyone so
        // use the same naming schemes as other libs and keep the prefix
        val width = numericPart.length
        String.format("%0${width}d", asNumber + 1)
    } else {
        (asNumber + 1).toString()
    }

    components.add("$prefix$nextPart")
    return components.joinToString(".") + "-SNAPSHOT"
}

fun setVersionInDocs(version: String, nextSnapshot: String) {
    for (file in File("docs/source").walk() + File("README.md")) {
        if (file.isDirectory || !(file.name.endsWith(".md") || file.name.endsWith(".mdx"))) continue

        val content = file.readText()
            // Dependencies
            .replace(Regex(""""com\.lightspark:(.+):.+"""")) {
                """"com.lightspark:${it.groupValues[1]}:$version""""
            }
            // Badge link
            .replace(Regex("artifact/com.lightspark/compose-qr-code/.+\\)")) {
                "artifact/com.lightspark/compose-qr-code/$version)"
            }
        file.writeText(content)
    }
}