package com.intellij.debugger.streams.reactive.agent.lib.rxjava

import com.intellij.debugger.streams.reactive.agent.lib.LibrarySupport
import com.intellij.debugger.streams.reactive.agent.lib.ReactiveDebuggerMethodVisitorBase
import net.bytebuddy.jar.asm.MethodVisitor
import net.bytebuddy.jar.asm.Opcodes
import net.bytebuddy.jar.asm.Type
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Aleksandr Eslikov
 */
class RxJavaMethodVisitor(
    methodVisitor: MethodVisitor,
    private val librarySupport: LibrarySupport,
    currentFileName: String?,
    changed: AtomicBoolean
) : ReactiveDebuggerMethodVisitorBase(methodVisitor, librarySupport, currentFileName, changed) {

    val operationId = AtomicInteger()

    override fun visitDoOnNext() {
        super.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "io/reactivex/rxjava3/core/Observable",
            "doOnNext",
            "(Lio/reactivex/rxjava3/functions/Consumer;)Lio/reactivex/rxjava3/core/Observable;",
            false
        )
    }

    override fun shouldModify(ownerClassName: String, descriptor: String, currentMethodName: String): Boolean {
        if (!isRxJavaPublisher(ownerClassName)) {
            return false
        }

        val returnType = Type.getReturnType(descriptor).internalName
        if (returnType.startsWith("io/reactivex/rxjava3/core/")) {
            return true
        }

        if (librarySupport.visitMethodHandlerFactory.getHandler(currentMethodName) != null) {
            return true
        }

        return false
    }


    private fun isRxJavaPublisher(className: String?): Boolean {
        return when (className) {
            "io/reactivex/rxjava3/core/Observable" -> true
            else -> false
        }
    }
}
