/*
 * Copyright (c) 2023, the fbase-options-gradle-plugin project authors and contributors.
 * Please see the AUTHORS file for details.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package ru.pixnews.gradle.fbase.options.util

import java.io.File
import java.util.Properties

internal fun File.toProperties(): Properties = Properties().apply {
    this@toProperties.bufferedReader().use { load(it) }
}