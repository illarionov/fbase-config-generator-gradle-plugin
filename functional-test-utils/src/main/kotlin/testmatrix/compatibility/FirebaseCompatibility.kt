/*
 * Copyright (c) 2023-2024, the fbase-config-generator-gradle-plugin project authors and contributors.
 * Please see the AUTHORS file for details.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package ru.pixnews.gradle.fbase.test.functional.testmatrix.compatibility

import ru.pixnews.gradle.fbase.test.functional.testmatrix.Version

// https://firebase.google.com/support/release-notes/android
public object FirebaseCompatibility {
    public val FIREBASE_BOM_33_1_2: Version = Version(33, 1, 2)
    public val FIREBASE_BOM_33_0_0: Version = Version(33, 0, 0)
    public val FIREBASE_BOM_32_8_1: Version = Version(32, 8, 1)
    public val FIREBASE_BOM_32_7_4: Version = Version(32, 7, 4)
    public val FIREBASE_BOM_32_6_0: Version = Version(32, 6, 0)
    public val FIREBASE_BOM_32_5_0: Version = Version(32, 5, 0)
    public val FIREBASE_BOM_32_4_1: Version = Version(32, 4, 1)
    public val FIREBASE_BOM_32_3_1: Version = Version(32, 3, 1)
}
