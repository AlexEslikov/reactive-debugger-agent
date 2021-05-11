// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.debugger.streams.reactive.agent.lib

/**
 * @author Aleksandr Eslikov
 */
interface ClassFilter {
    /**
     * Checks if [className] should not be transformed
     */
    fun shouldSkipTransformation(className: String): Boolean
}
