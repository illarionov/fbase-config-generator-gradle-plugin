/*
 * Copyright (c) 2023, the fbase-options-gradle-plugin project authors and contributors.
 * Please see the AUTHORS file for details.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package ru.pixnews.gradle.fbase.options

import com.android.build.api.variant.VariantExtension
import com.android.build.api.variant.VariantExtensionConfig
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.provider.Property
import ru.pixnews.gradle.fbase.options.util.VariantDefaults
import java.io.Serializable
import javax.inject.Inject

abstract class FirebaseConfigGeneratorExtension @Inject internal constructor(
    project: Project,
    extensionConfig: VariantExtensionConfig<*>?,
) : Serializable, VariantExtension {
    /**
     * Should the google_app_id string parameter be added to Android resources.
     * Enabled by default.
     * Android string resource "google_app_id" will be initialized with the value from the first configuration defined.
     */
    abstract val addGoogleAppIdResource: Property<Boolean>
    abstract val configurations: NamedDomainObjectContainer<FirebaseOptionsExtension>

    @Transient
    val providers: FirebaseOptionsProviders

    init {
        val variantDefaults = extensionConfig?.variant?.let { VariantDefaults(project.providers, it) }
        val defaultApplicationIdProvider = if (variantDefaults != null) {
            variantDefaults.applicationId
        } else {
            project.providers.provider { "" }
        }

        providers = FirebaseOptionsProviders(project, defaultApplicationIdProvider)
    }

    companion object {
        private const val serialVersionUID: Long = -1
    }
}
