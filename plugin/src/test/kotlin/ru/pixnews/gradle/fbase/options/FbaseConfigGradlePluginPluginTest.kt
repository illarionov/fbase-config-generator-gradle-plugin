/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package ru.pixnews.gradle.fbase.options

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

/**
 * A simple unit test for the 'ru.pixnews.gradle.fbase.config.greeting' plugin.
 */
class FbaseConfigGradlePluginPluginTest {
    @Test
    fun `plugin registers task`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("ru.pixnews.gradle.fbase.options")

        // TODO Verify the result
        // assertNotNull(project.tasks.findByName("greeting"))
    }
}
