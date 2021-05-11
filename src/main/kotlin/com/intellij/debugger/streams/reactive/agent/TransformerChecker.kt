// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.debugger.streams.reactive.agent

import com.intellij.debugger.streams.reactive.agent.lib.LibrarySupportProvider

/**
 * @author Aleksandr Eslikov
 */
internal object TransformerChecker {
    fun shouldTransform(loader: ClassLoader?, className: String?, clazz: Class<*>?): Boolean {
        if (className.isNullOrEmpty()) return false
        if (loader == null) return false
        if (isJdkClass(className)) return false
        if (isClassTypePrimitive(clazz)) return false
        if (librarySpecificSkip(className)) return false
        return true
    }

    private fun librarySpecificSkip(className: String): Boolean {
        LibrarySupportProvider.getProviders().forEach { provider ->
            if (provider.classFilter.shouldSkipTransformation(className)) return true
        }
        return false
    }

    private fun isClassTypePrimitive(clazz: Class<*>?): Boolean {
        if (clazz != null && (clazz.isPrimitive ||
                    clazz.isArray ||
                    clazz.isAnnotation ||
                    clazz.isSynthetic)
        ) {
            return true
        }
        return false
    }


    private fun isJdkClass(className: String): Boolean {
        if (className.startsWith("[") ||
            className.startsWith("java/") ||
            className.startsWith("java.") ||
            className.startsWith("jdk/") ||
            className.startsWith("jdk.") ||
            className.startsWith("sun/") ||
            className.startsWith("sun.") ||
            className.startsWith("com/sun/") ||
            className.startsWith("com.sun.")
        ) {
            return true
        }
        return false
    }
}
