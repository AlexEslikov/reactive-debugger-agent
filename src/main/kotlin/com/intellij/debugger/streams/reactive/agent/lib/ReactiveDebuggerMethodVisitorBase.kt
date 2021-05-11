package com.intellij.debugger.streams.reactive.agent.lib


import net.bytebuddy.jar.asm.Label
import net.bytebuddy.jar.asm.MethodVisitor
import net.bytebuddy.jar.asm.Opcodes
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Aleksandr Eslikov
 */
abstract class ReactiveDebuggerMethodVisitorBase(
    methodVisitor: MethodVisitor,
    private val librarySupport: LibrarySupport,
    private val currentFileName: String?,
    private val changed: AtomicBoolean
) :
    MethodVisitor(Opcodes.ASM7, methodVisitor), DoOnNextVisitor {

    private val operationId = AtomicInteger()
    private var currentLine: Int? = null

    abstract fun shouldModify(ownerClassName: String, descriptor: String, currentMethodName: String): Boolean

    override fun visitLineNumber(line: Int, start: Label?) {
        super.visitLineNumber(line, start)
        currentLine = line
    }

    override fun visitMethodInsn(
        opcode: Int,
        owner: String,
        name: String,
        descriptor: String,
        isInterface: Boolean
    ) {
        val visitMethodHandlerFactory = librarySupport.visitMethodHandlerFactory
        val handler = visitMethodHandlerFactory.getHandler(name)
        if (!shouldModify(owner, descriptor, name) || handler == null) {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
            return
        }

        val context = MethodContext(getStreamId(), operationId, librarySupport)

        handler.transformMethodCall(
            this,
            context
        ) { super.visitMethodInsn(opcode, owner, name, descriptor, isInterface) }

        changed.set(true)

    }

    fun visitNewLoggingConsumerBeforeOperation(
        streamId: String,
        currentId: Int,
        consumerType: String
    ) {
        visitTypeInsn(Opcodes.NEW, consumerType)
        visitInsn(Opcodes.DUP)
        visitLdcInsn(streamId)
        visitIntInsn(Opcodes.SIPUSH, currentId)
        visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            consumerType,
            "<init>",
            "(Ljava/lang/String;I)V",
            false
        )
    }

    fun visitNewLoggingConsumerAfterOperation(
        streamId: String,
        currentId: Int,
        tickOnly: Boolean,
        consumerType: String
    ) {
        visitTypeInsn(Opcodes.NEW, consumerType)
        visitInsn(Opcodes.DUP)
        visitLdcInsn(streamId)
        visitIntInsn(Opcodes.SIPUSH, currentId)
        if (tickOnly) {
            visitInsn(Opcodes.ICONST_1)
        } else {
            visitInsn(Opcodes.ICONST_0)
        }
        visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            consumerType,
            "<init>",
            "(Ljava/lang/String;IZ)V",
            false
        )
    }

    private fun getStreamId(): String {
        return "$currentFileName:$currentLine"
    }
}
