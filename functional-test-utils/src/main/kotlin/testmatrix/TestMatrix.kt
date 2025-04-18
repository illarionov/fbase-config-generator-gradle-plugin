/*
 * Copyright (c) 2023-2024, the fbase-config-generator-gradle-plugin project authors and contributors.
 * Please see the AUTHORS file for details.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package ru.pixnews.gradle.fbase.test.functional.testmatrix

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.AgpVersionCompatibility.AGP_8_10_0_ALPHA01
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.AgpVersionCompatibility.AGP_8_4_2
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.AgpVersionCompatibility.AGP_8_5_2
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.AgpVersionCompatibility.AGP_8_6_1
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.AgpVersionCompatibility.AGP_8_7_3
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.AgpVersionCompatibility.AGP_8_8_0
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.AgpVersionCompatibility.AGP_8_9_0_BETA01
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.AgpVersionCompatibility.agpIsCompatibleWithGradle
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.AgpVersionCompatibility.getCompatibleAndroidApiLevel
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.AgpVersionCompatibility.isAgpCompatibleWithRuntime
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.FirebaseCompatibility.FIREBASE_BOM_32_7_4
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.FirebaseCompatibility.FIREBASE_BOM_32_8_1
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.FirebaseCompatibility.FIREBASE_BOM_33_0_0
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.FirebaseCompatibility.FIREBASE_BOM_33_1_2
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.FirebaseCompatibility.FIREBASE_BOM_33_8_0
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.GradleVersionCompatibility.GRADLE_8_10_2
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.GradleVersionCompatibility.GRADLE_8_11_1
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.GradleVersionCompatibility.GRADLE_8_12
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.GradleVersionCompatibility.GRADLE_8_5
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.GradleVersionCompatibility.GRADLE_8_6
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.GradleVersionCompatibility.GRADLE_8_7
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.GradleVersionCompatibility.GRADLE_8_8
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.GradleVersionCompatibility.GRADLE_8_9
import ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility.GradleVersionCompatibility.isGradleCompatibleWithRuntime

public class TestMatrix(
    fbasePluginVersion: String,
) {
    private val logger: Logger = LoggerFactory.getLogger(TestMatrix::class.java)
    private val defaultVersionCatalog: VersionCatalog = VersionCatalog.getDefault(fbasePluginVersion)
    private val gradleVersions = listOf(
        GRADLE_8_12,
        GRADLE_8_11_1,
        GRADLE_8_10_2,
        GRADLE_8_9,
        GRADLE_8_8,
        GRADLE_8_7,
        GRADLE_8_6,
        GRADLE_8_5,
    )

    // See https://developer.android.com/studio/releases/gradle-plugin
    private val agpVersions = listOf(
        AGP_8_10_0_ALPHA01,
        AGP_8_9_0_BETA01,
        AGP_8_8_0,
        AGP_8_7_3,
        AGP_8_6_1,
        AGP_8_5_2,
        AGP_8_4_2,
    )
    private val firebaseVersions = listOf(
        FIREBASE_BOM_33_8_0,
        FIREBASE_BOM_33_1_2,
        FIREBASE_BOM_33_0_0,
        FIREBASE_BOM_32_8_1,
        FIREBASE_BOM_32_7_4,
    )

    public fun getMainTestVariants(): List<VersionCatalog> = getCompatibleGradleAgpVariants()
        .map { (gradleVersion, agpVersion) ->
            val compileTargetSdk = getCompatibleAndroidApiLevel(agpVersion)
            defaultVersionCatalog.copy(
                gradleVersion = gradleVersion,
                agpVersion = agpVersion,
                compileSdk = compileTargetSdk,
                targetSdk = compileTargetSdk,
            )
        }
        .toList()
        .also { catalogs ->
            require(catalogs.isNotEmpty()) {
                "Found no compatible AGP and Gradle version combination, check your supplied arguments."
            }
        }

    public fun getFirebaseTestVariants(): List<VersionCatalog> = firebaseVersions().map {
        defaultVersionCatalog.copy(firebaseVersion = it)
    }

    private fun getCompatibleGradleAgpVariants(): Sequence<Pair<Version, Version>> {
        val (gradleCompatibleVersions, gradleIncompatibleVersions) = gradleVersions().partition {
            isGradleCompatibleWithRuntime(it.baseVersion())
        }

        if (gradleIncompatibleVersions.isNotEmpty()) {
            logger.warn(
                "Gradle versions {} cannot be run on the current JVM `{}`",
                gradleIncompatibleVersions.joinToString(),
                Runtime.version(),
            )
        }

        val (agpCompatibleVersions, agpIncompatibleVersions) = agpVersions().partition {
            isAgpCompatibleWithRuntime(it)
        }

        if (agpIncompatibleVersions.isNotEmpty()) {
            logger.warn(
                "Android Gradle Plugin versions {} cannot be run on the current JVM `{}`",
                agpIncompatibleVersions.joinToString(),
                Runtime.version(),
            )
        }

        return sequence {
            gradleCompatibleVersions.forEach { gradleVersion ->
                agpCompatibleVersions.forEach { agpVersion ->
                    yield(gradleVersion to agpVersion)
                }
            }
        }.filter { (gradleVersion, agpVersion) ->
            agpIsCompatibleWithGradle(agpVersion, gradleVersion)
        }
    }

    // Allow setting a single, fixed Gradle version via environment variables
    private fun gradleVersions(): List<Version> {
        val gradleVersion = System.getenv("GRADLE_VERSION")
        return if (gradleVersion == null) {
            gradleVersions
        } else {
            listOf(Version.parse(gradleVersion))
        }
    }

    // Allow setting a single, fixed AGP version via environment variables
    private fun agpVersions(): List<Version> {
        val agpVersion = System.getenv("AGP_VERSION")
        return if (agpVersion == null) {
            agpVersions
        } else {
            listOf(Version.parse(agpVersion))
        }
    }

    private fun firebaseVersions(): List<Version> {
        val firebaseVersion = System.getenv("FIREBASE_VERSION")
        return if (firebaseVersion == null) {
            firebaseVersions
        } else {
            listOf(Version.parse(firebaseVersion))
        }
    }
}
