package com.intellij.debugger.streams.reactive.agent.lib.operators.impl

import com.intellij.debugger.streams.reactive.agent.lib.ReactiveDebuggerMethodVisitorBase
import com.intellij.debugger.streams.reactive.agent.lib.MethodContext
import com.intellij.debugger.streams.reactive.agent.lib.operators.Operator

/**
 * @author Aleksandr Eslikov
 */
class TerminateOperator(override val name: String) : Operator {
    override fun <T : ReactiveDebuggerMethodVisitorBase> transformMethodCall(
        methodVisitor: T,
        methodContext: MethodContext,
        originalMethod: T.() -> Unit
    ) {
        val currentOperationId = methodContext.operationId.incrementAndGet()
        methodVisitor.visitNewLoggingConsumerBeforeOperation(
            methodContext.currentStreamId,
            currentOperationId,
            methodContext.librarySupport.consumerBeforeType
        )
        methodVisitor.visitDoOnNext()

        originalMethod(methodVisitor)
    }
}
