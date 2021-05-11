package com.intellij.debugger.streams.reactive.agent.lib.operators.impl

import com.intellij.debugger.streams.reactive.agent.lib.ReactiveDebuggerMethodVisitorBase
import com.intellij.debugger.streams.reactive.agent.lib.MethodContext
import com.intellij.debugger.streams.reactive.agent.lib.operators.Operator

/**
 * @author Aleksandr Eslikov
 */
class PublisherOperator(override val name: String) : Operator {

    override fun <T : ReactiveDebuggerMethodVisitorBase> transformMethodCall(
        methodVisitor: T,
        methodContext: MethodContext,
        originalMethod: T.() -> Unit
    ) {
        val currentOperationId = methodContext.operationId.incrementAndGet()

        originalMethod(methodVisitor)

        methodVisitor.visitNewLoggingConsumerAfterOperation(
            methodContext.currentStreamId,
            currentOperationId,
            true,
            methodContext.librarySupport.consumerAfterType
        )
        methodVisitor.visitDoOnNext()
    }
}
