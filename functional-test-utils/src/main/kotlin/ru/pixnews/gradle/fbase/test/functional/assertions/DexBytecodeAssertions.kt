/*
 * Copyright (c) 2023, the fbase-config-generator-gradle-plugin project authors and contributors.
 * Please see the AUTHORS file for details.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package ru.pixnews.gradle.fbase.test.functional.assertions

import assertk.Assert
import assertk.assertions.contains
import assertk.assertions.isNotNull
import ru.pixnews.gradle.fbase.test.functional.TestFirebaseOptions

public fun Assert<String?>.dexBytecodeMatch(
    options: TestFirebaseOptions,
): Unit = this
    .isNotNull()
    .transform { dexBytecodeAsString ->
        dexBytecodeAsString.split("\n")
            .map(String::trim)
            .filter(String::isNotEmpty)
            .filterNot(lineRegex::matches)
            .joinToString("\n")
    }
    .run {
        val templates = propToBuilderMethodName.mapNotNull { (prop, builderMethodName) ->
            val propValue = prop(options)
            if (propValue != null) {
                getMatchBuilderMethod(builderMethodName, propValue)
            } else {
                null
            }
        }
        templates.forEach {
            contains(it)
        }
    }

private fun getMatchBuilderMethod(
    methodName: String,
    expectedValue: String,
): String {
    val builderFqcn = "Lcom/google/firebase/FirebaseOptions\$Builder;"
    val invokeMethodSmali = "invoke-virtual {v0, v1}, $builderFqcn->$methodName(Ljava/lang/String;)$builderFqcn"
    return "const-string v1, \"$expectedValue\"\n$invokeMethodSmali"
}

private val lineRegex = """\s*\.line \d+""".toRegex()

private val propToBuilderMethodName = listOf(
    TestFirebaseOptions::projectId to "setProjectId",
    TestFirebaseOptions::apiKey to "setApiKey",
    TestFirebaseOptions::applicationId to "setApplicationId",
    TestFirebaseOptions::databaseUrl to "setDatabaseUrl",
    TestFirebaseOptions::gaTrackingId to "setGaTrackingId",
    TestFirebaseOptions::gcmSenderId to "setGcmSenderId",
    TestFirebaseOptions::storageBucket to "setStorageBucket",
)
