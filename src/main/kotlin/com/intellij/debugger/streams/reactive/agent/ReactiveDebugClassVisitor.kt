// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.debugger.streams.reactive.agent

import net.bytebuddy.jar.asm.ClassVisitor
import net.bytebuddy.jar.asm.MethodVisitor
import net.bytebuddy.jar.asm.Opcodes
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Aleksandr Eslikov
 */
internal class ReactiveDebugClassVisitor(classVisitor: ClassVisitor, private val changed: AtomicBoolean) :
    ClassVisitor(Opcodes.ASM7, classVisitor) {

    private var currentClassName: String? = null
    private var currentFileName: String? = null

    override fun visitSource(source: String?, debug: String?) {
        super.visitSource(source, debug)
        currentFileName = source
    }

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        currentClassName = name
    }

    override fun visitMethod(
        access: Int,
        currentMethod: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        return super.visitMethod(access, currentMethod, descriptor, signature, exceptions)
    }
}
