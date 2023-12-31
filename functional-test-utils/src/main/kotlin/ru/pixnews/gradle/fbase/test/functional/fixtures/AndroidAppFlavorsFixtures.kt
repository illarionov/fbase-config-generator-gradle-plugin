/*
 * Copyright (c) 2023, the fbase-config-generator-gradle-plugin project authors and contributors.
 * Please see the AUTHORS file for details.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package ru.pixnews.gradle.fbase.test.functional.fixtures

import ru.pixnews.gradle.fbase.test.functional.TestFirebaseOptions

public object AndroidAppFlavorsFixtures {
    public val firebaseProperties: TestFirebaseOptions = TestFirebaseOptions(
        projectId = "sample-en",
        apiKey = "AIzbSyCILMsOuUKwN3qhtxrPq7FFemDJUAXTyZ8",
        applicationId = "1:1035469437089:android:73a4fb8297b2cd4f",
        databaseUrl = "https://sample-en.firebaseio.com",
        gaTrackingId = "UA-65557217-3",
        gcmSenderId = "1035469437089",
        storageBucket = "sample-en.appspot.com",
    )
    public val firebaseBenchmarkProperties: TestFirebaseOptions = TestFirebaseOptions(
        projectId = "sample-benchmark-en",
        apiKey = "AIzbSyCILMsOuUKwN3qhtxrPq7FFemDJUAXTyZ9",
        applicationId = "1:1035469437089:android:73a4fb8297b2cd50",
        databaseUrl = "https://sample-benchmark-en.firebaseio.com",
        gaTrackingId = "UA-65557217-5",
        gcmSenderId = "1035469437090",
        storageBucket = "sample-benchmark-en.appspot.com",
    )
    public val firebaseDemoProperties: TestFirebaseOptions = TestFirebaseOptions(
        projectId = "sample-demo-en",
        apiKey = "AIzbSyCILMsOuUKwN3qhtxrPq7FFemDJUAXTyZA",
        applicationId = "1:1035469437089:android:73a4fb8297b2cd51",
        databaseUrl = "https://sample-demo-en.firebaseio.com",
        gaTrackingId = "UA-65557217-6",
        gcmSenderId = "1035469437091",
        storageBucket = "sample-demo-en.appspot.com",
    )
    public val firebaseFullProperties: TestFirebaseOptions = TestFirebaseOptions(
        projectId = "sample-full-en",
        apiKey = "AIzbSyCILMsOuUKwN3qhtxrPq7FFemDJUAXTyZB",
        applicationId = "1:1035469437089:android:73a4fb8297b2cd52",
        databaseUrl = "https://sample-full-en.firebaseio.com",
        gaTrackingId = "UA-65557217-7",
        gcmSenderId = "1035469437092",
        storageBucket = "sample-full-en.appspot.com",
    )
    public val firebaseMinApi21Properties: TestFirebaseOptions = TestFirebaseOptions(
        projectId = "sample-minapi21-en",
        apiKey = "AIzbSyCILMsOuUKwN3qhtxrPq7FFemDJUAXTyZC",
        applicationId = "1:1035469437089:android:73a4fb8297b2cd53",
        databaseUrl = "https://sample-minapi21-en.firebaseio.com",
        gaTrackingId = "UA-65557217-8",
        gcmSenderId = "1035469437093",
        storageBucket = "sample-minapi21-en.appspot.com",
    )
    public val firebaseMinApi24Properties: TestFirebaseOptions = TestFirebaseOptions(
        projectId = "sample-minapi24-en",
        apiKey = "AIzbSyCILMsOuUKwN3qhtxrPq7FFemDJUAXTyZD",
        applicationId = "1:1035469437089:android:73a4fb8297b2cd54",
        databaseUrl = "https://sample-minapi24-en.firebaseio.com",
        gaTrackingId = "UA-65557217-9",
        gcmSenderId = "1035469437094",
        storageBucket = "sample-minapi24-en.appspot.com",
    )
    public val firebaseReleaseProperties: TestFirebaseOptions = TestFirebaseOptions(
        projectId = "sample-release-en",
        apiKey = "AIzbSyCILMsOuUKwN3qhtxrPq7FFemDJUAXTyZE",
        applicationId = "1:1035469437089:android:73a4fb8297b2cd55",
        databaseUrl = "https://sample-release-en.firebaseio.com",
        gaTrackingId = "UA-65557218-1",
        gcmSenderId = "1035469437095",
        storageBucket = "sample-release-en.appspot.com",
    )
    public val testedVariants: List<AppFlavorsVariant> = buildList {
        listOf("minApi21", "minApi24").forEach { api ->
            listOf("demo", "full").forEach { mode ->
                listOf("debug", "benchmark", "release").forEach { buildType ->
                    val expectedGoogleAppId = when (api) {
                        "minApi21" -> firebaseMinApi21Properties.applicationId!!
                        "minApi24" -> firebaseMinApi24Properties.applicationId!!
                        else -> error("Unknown api")
                    }

                    val variant = AppFlavorsVariant(
                        buildType = buildType,
                        flavors = listOf(api, mode),
                        expectedGoogleAppId = expectedGoogleAppId,
                        expectedBuilders = getExpectedBuilders(api, mode, buildType),
                    )
                    add(variant)
                }
            }
        }
    }

    private fun getExpectedBuilders(
        api: String,
        mode: String,
        buildType: String,
    ): List<ExpectedBuilder> = buildList {
        when (api) {
            "minApi21" -> add(ExpectedBuilder("MinApi21FirebaseOptionsKt", firebaseMinApi21Properties))
            "minApi24" -> add(ExpectedBuilder("MinApi24FirebaseOptionsKt", firebaseMinApi24Properties))
        }
        when (mode) {
            "demo" -> add(ExpectedBuilder("DemoFirebaseOptionsKt", firebaseDemoProperties))
            "full" -> add(ExpectedBuilder("FullFirebaseOptionsKt", firebaseFullProperties))
        }
        when (buildType) {
            "release" -> add(ExpectedBuilder("ReleaseFirebaseOptionsKt", firebaseReleaseProperties))
            "benchmark" -> add(ExpectedBuilder("BenchmarkFirebaseOptionsKt", firebaseBenchmarkProperties))
        }
        add(ExpectedBuilder("FirebaseOptionsKt", firebaseProperties))
    }

    public data class AppFlavorsVariant(
        val buildType: String,
        val flavors: List<String>,
        val expectedGoogleAppId: String,
        val expectedBuilders: List<ExpectedBuilder>,
    )

    public data class ExpectedBuilder(
        val className: String,
        val properties: TestFirebaseOptions,
    )
}
