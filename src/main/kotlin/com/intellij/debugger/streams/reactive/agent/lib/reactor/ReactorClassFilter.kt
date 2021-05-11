// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.debugger.streams.reactive.agent.lib.reactor

import com.intellij.debugger.streams.reactive.agent.lib.ClassFilter

/**
 * @author Aleksandr Eslikov
 */
class ReactorClassFilter : ClassFilter {
    override fun shouldSkipTransformation(className: String): Boolean {
        if (className.startsWith("reactor.core.") ||
            className.startsWith("reactor/core/")
        ) {
            return true
        }
        return false
    }
}
