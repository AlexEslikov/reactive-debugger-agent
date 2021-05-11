package com.intellij.debugger.streams.reactive.agent.lib.operators.impl

import com.intellij.debugger.streams.reactive.agent.lib.ReactiveDebuggerMethodVisitorBase
import com.intellij.debugger.streams.reactive.agent.lib.MethodContext
import com.intellij.debugger.streams.reactive.agent.lib.operators.Operator
import net.bytebuddy.jar.asm.Opcodes

/**
 * Operator with two-word operands
 *
 * @author Aleksandr Eslikov
 */
class LongOperator(override val name: String) : Operator {

    override fun <T : ReactiveDebuggerMethodVisitorBase> transformMethodCall(
        methodVisitor: T,
        methodContext: MethodContext,
        originalMethod: T.() -> Unit
    ) {
        val currentOperationId = methodContext.operationId.incrementAndGet()
        //swap long and reference
        methodVisitor.visitInsn(Opcodes.DUP2_X1)
        methodVisitor.visitInsn(Opcodes.POP2)

        methodVisitor.visitNewLoggingConsumerBeforeOperation(
            methodContext.currentStreamId,
            currentOperationId,
            methodContext.librarySupport.consumerBeforeType
        )
        methodVisitor.visitDoOnNext()

        //swap back
        methodVisitor.visitInsn(Opcodes.DUP_X2)
        methodVisitor.visitInsn(Opcodes.POP)

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
