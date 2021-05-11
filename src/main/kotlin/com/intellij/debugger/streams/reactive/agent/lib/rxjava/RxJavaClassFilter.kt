// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.debugger.streams.reactive.agent.lib.rxjava

import com.intellij.debugger.streams.reactive.agent.lib.ClassFilter

/**
 * @author Aleksandr Eslikov
 */
class RxJavaClassFilter : ClassFilter {
    override fun shouldSkipTransformation(className: String): Boolean {
        if (className.startsWith("io.reactivex.rxjava3.") ||
            className.startsWith("io/reactivex/rxjava3/")
        ) {
            return true
        }
        return false
    }
}
