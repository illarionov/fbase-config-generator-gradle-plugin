/*
 * Copyright (c) 2023, the fbase-options-gradle-plugin project authors and contributors.
 * Please see the AUTHORS file for details.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package ru.pixnews.gradle.fbase.options

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

/**
 * A simple functional test for the 'ru.pixnews.gradle.fbase.config.greeting' plugin.
 */
class FbaseConfigGradlePluginPluginFunctionalTest {
    private val buildFile by lazy { projectDir.resolve("build.gradle") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle") }

    @field:TempDir
    lateinit var projectDir: File

    @Test
    fun `can run task`() {
        // Set up the test build
        settingsFile.writeText("")
        buildFile.writeText(
            """
            plugins {
                id('ru.pixnews.gradle.fbase.options')
            }
        """.trimIndent(),
        )

        // Run the build
        val runner = GradleRunner.create().apply {
            forwardOutput()
            withPluginClasspath()
            withArguments(
                "--stacktrace",
                "--info",
                "greeting",
            )
            withProjectDir(projectDir)
        }

        val result = runner.build()

        // TODO Verify the result
        assertTrue(result.output.contains("OK"))
    }
}
