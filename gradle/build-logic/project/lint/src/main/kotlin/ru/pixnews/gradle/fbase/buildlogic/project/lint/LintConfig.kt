/*
 * Copyright (c) 2023, the fbase-options-gradle-plugin project authors and contributors.
 * Please see the AUTHORS file for details.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package ru.pixnews.gradle.fbase.buildlogic.project.lint

import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.util.PatternFilterable

internal val Project.configRootDir: Directory
    get() {
        return rootProject.layout.projectDirectory.dir("config")
    }

internal val Project.lintedFileTree: FileTree
    get() = rootProject.layout.projectDirectory.asFileTree.matching {
        excludeNonLintedDirectories()
    }

internal fun PatternFilterable.excludeNonLintedDirectories() {
    exclude {
        it.isDirectory && it.name in excludedDirectories
    }
    exclude {
        it.isDirectory && it.relativePath.startsWith("config/copyright")
    }
    exclude("**/api/**/*.api")
}

private val excludedDirectories = setOf(
    ".git",
    ".gradle",
    ".idea",
    "build",
    "generated",
    "out",
)
