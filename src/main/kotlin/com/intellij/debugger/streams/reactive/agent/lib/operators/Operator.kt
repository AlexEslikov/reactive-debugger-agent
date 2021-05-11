package com.intellij.debugger.streams.reactive.agent.lib.operators

import com.intellij.debugger.streams.reactive.agent.lib.MethodContext
import com.intellij.debugger.streams.reactive.agent.lib.ReactiveDebuggerMethodVisitorBase

/**
 * @author Aleksandr Eslikov
 */
interface Operator {
    val name: String

    /**
     * Surround method call with logging operators
     */
    fun <T> transformMethodCall(
        methodVisitor: T,
        methodContext: MethodContext,
        originalMethod: T.() -> Unit
    )
            where T : ReactiveDebuggerMethodVisitorBase
}
