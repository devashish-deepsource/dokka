package org.jetbrains.dokka.analysis.kotlin.descriptors.compiler.impl

import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.analysis.kotlin.descriptors.compiler.configuration.DescriptorDocumentableSource
import org.jetbrains.dokka.model.Documentable
import org.jetbrains.dokka.model.WithSources
import org.jetbrains.kotlin.analysis.kotlin.internal.SyntheticDocumentableDetector
import org.jetbrains.kotlin.descriptors.CallableMemberDescriptor

internal class DescriptorSyntheticDocumentableDetector : SyntheticDocumentableDetector {
    override fun isSynthetic(documentable: Documentable, sourceSet: DokkaConfiguration.DokkaSourceSet): Boolean {
        return isFakeOverride(documentable, sourceSet) || isSynthesized(documentable, sourceSet)
    }

    private fun isFakeOverride(documentable: Documentable, sourceSet: DokkaConfiguration.DokkaSourceSet): Boolean {
        return callableMemberDescriptorOrNull(documentable, sourceSet)?.kind == CallableMemberDescriptor.Kind.FAKE_OVERRIDE
    }

    private fun isSynthesized(documentable: Documentable, sourceSet: DokkaConfiguration.DokkaSourceSet): Boolean {
        return callableMemberDescriptorOrNull(documentable, sourceSet)?.kind == CallableMemberDescriptor.Kind.SYNTHESIZED
    }

    private fun callableMemberDescriptorOrNull(
        documentable: Documentable, sourceSet: DokkaConfiguration.DokkaSourceSet
    ): CallableMemberDescriptor? {
        if (documentable is WithSources) {
            return documentable.sources[sourceSet]
                .let { it as? DescriptorDocumentableSource }?.descriptor as? CallableMemberDescriptor
        }

        return null
    }
}
