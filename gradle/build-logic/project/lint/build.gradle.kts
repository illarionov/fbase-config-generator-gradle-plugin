/*
 * Copyright (c) 2023, the fbase-config-generator-gradle-plugin project authors and contributors.
 * Please see the AUTHORS file for details.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

plugins {
    `kotlin-dsl`
}

group = "ru.pixnews.gradle.fbase.buildlogic.project.lint"

dependencies {
    implementation(libs.agp.plugin.api)
    implementation(libs.detekt.plugin)
    implementation(libs.diktat.plugin)
    implementation(libs.spotless.plugin)
}
