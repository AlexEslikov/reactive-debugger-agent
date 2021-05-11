package com.intellij.debugger.streams.reactive.agent.lib.reactor

import com.intellij.debugger.streams.reactive.agent.lib.LibrarySupport
import com.intellij.debugger.streams.reactive.agent.lib.MethodVisitorFactory
import net.bytebuddy.jar.asm.MethodVisitor
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Aleksandr Eslikov
 */
class ReactorMethodVisitorFactory : MethodVisitorFactory {
    override fun getMethodVisitor(
        methodVisitor: MethodVisitor,
        librarySupport: LibrarySupport,
        currentClassName: String?,
        currentMethod: String?,
        descriptor: String?,
        currentFileName: String?,
        changed: AtomicBoolean
    ): MethodVisitor {
        return ReactorMethodVisitor(
            methodVisitor,
            librarySupport,
            currentFileName,
            changed
        )
    }
}
