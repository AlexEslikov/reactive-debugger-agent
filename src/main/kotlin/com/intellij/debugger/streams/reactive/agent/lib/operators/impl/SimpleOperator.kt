package com.intellij.debugger.streams.reactive.agent.lib.operators.impl

import com.intellij.debugger.streams.reactive.agent.lib.ReactiveDebuggerMethodVisitorBase
import com.intellij.debugger.streams.reactive.agent.lib.MethodContext
import com.intellij.debugger.streams.reactive.agent.lib.operators.Operator
import net.bytebuddy.jar.asm.Opcodes

/**
 * Operator with one-word operands
 *
 * @author Aleksandr Eslikov
 */
class SimpleOperator(override val name: String) : Operator {

    override fun <T : ReactiveDebuggerMethodVisitorBase> transformMethodCall(
        methodVisitor: T,
        methodContext: MethodContext,
        originalMethod: T.() -> Unit
    ) {
        val currentOperationId = methodContext.operationId.incrementAndGet()
        methodVisitor.visitInsn(Opcodes.SWAP)
        methodVisitor.visitNewLoggingConsumerBeforeOperation(
            methodContext.currentStreamId,
            currentOperationId,
            methodContext.librarySupport.consumerBeforeType
        )
        methodVisitor.visitDoOnNext()
        methodVisitor.visitInsn(Opcodes.SWAP)

        originalMethod(methodVisitor)

        methodVisitor.visitNewLoggingConsumerAfterOperation(
            methodContext.currentStreamId,
            currentOperationId,
            false,
            methodContext.librarySupport.consumerAfterType
        )

        methodVisitor.visitDoOnNext()
    }
}
