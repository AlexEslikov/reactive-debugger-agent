package com.intellij.debugger.streams.reactive.agent.lib.reactor

import com.intellij.debugger.streams.reactive.agent.lib.LibrarySupport
import com.intellij.debugger.streams.reactive.agent.lib.ReactiveDebuggerMethodVisitorBase
import net.bytebuddy.jar.asm.MethodVisitor
import net.bytebuddy.jar.asm.Opcodes
import net.bytebuddy.jar.asm.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Aleksandr Eslikov
 */
class ReactorMethodVisitor(
    methodVisitor: MethodVisitor,
    private val librarySupport: LibrarySupport,
    currentFileName: String?,
    changed: AtomicBoolean
) : ReactiveDebuggerMethodVisitorBase(methodVisitor, librarySupport, currentFileName, changed ) {

    override fun visitDoOnNext() {
        visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "reactor/core/publisher/Flux",
            "doOnNext",
            "(Ljava/util/function/Consumer;)Lreactor/core/publisher/Flux;",
            false
        )
    }


     override fun shouldModify(ownerClassName: String, descriptor: String, currentMethodName: String): Boolean {
        if (!isReactorPublisher(ownerClassName)) {
            return false
        }

        val returnType = Type.getReturnType(descriptor).internalName
        if (returnType.startsWith("reactor/core/publisher/")) {
            return true
        }

        if (librarySupport.visitMethodHandlerFactory.getHandler(currentMethodName) != null) {
            return true
        }

        return false
    }

    private fun isReactorPublisher(ownerClassName: String?): Boolean {
        return when (ownerClassName) {
            "reactor/core/publisher/Flux",
            "reactor/core/publisher/Mono",
            "reactor/core/publisher/ParallelFlux",
            "reactor/core/publisher/GroupedFlux" -> true
            else -> false
        }
    }
}
