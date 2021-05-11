package com.intellij.debugger.streams.reactive.agent.lib

import net.bytebuddy.jar.asm.MethodVisitor
import java.util.concurrent.atomic.AtomicBoolean

interface MethodVisitorFactory {
    fun getMethodVisitor(
        methodVisitor: MethodVisitor,
        librarySupport: LibrarySupport,
        currentClassName: String?,
        currentMethod: String?,
        descriptor: String?,
        currentFileName: String?,
        changed: AtomicBoolean
    ): MethodVisitor
}
