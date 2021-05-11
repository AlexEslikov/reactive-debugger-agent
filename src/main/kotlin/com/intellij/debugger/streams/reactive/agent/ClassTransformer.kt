// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.debugger.streams.reactive.agent

import com.intellij.debugger.streams.reactive.agent.TransformerChecker.shouldTransform
import net.bytebuddy.jar.asm.ClassReader
import net.bytebuddy.jar.asm.ClassVisitor
import net.bytebuddy.jar.asm.ClassWriter
import java.lang.instrument.ClassFileTransformer
import java.security.ProtectionDomain
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Transforms class files by applying [ReactiveDebugClassVisitor] to them
 *
 * @author Aleksandr Eslikov
 */
internal open class ClassTransformer : ClassFileTransformer {
    override fun transform(
        loader: ClassLoader?,
        className: String?,
        clazz: Class<*>?,
        protectionDomain: ProtectionDomain?,
        classFile: ByteArray?
    ): ByteArray? {

        if (!shouldTransform(loader, className, clazz)) {
            return null
        }

        val classReader = ClassReader(classFile)
        val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)

        val changed = AtomicBoolean()
        val classVisitor: ClassVisitor = ReactiveDebugClassVisitor(classWriter, changed)

        classReader.accept(classVisitor, 0)

        return if (!changed.get()) {
            null
        } else {
            classWriter.toByteArray()
        }
    }
}
