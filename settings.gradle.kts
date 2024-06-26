/*
 * Copyright (c) 2023, the fbase-config-generator-gradle-plugin project authors and contributors.
 * Please see the AUTHORS file for details.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */
pluginManagement {
    includeBuild("gradle/build-logic/settings")
}

plugins {
    id("ru.pixnews.gradle.fbase.buildlogic.settings.root")
}

rootProject.name = "fbase-config-generator-gradle-plugin"
include("generator")
include("functional-test-utils")
