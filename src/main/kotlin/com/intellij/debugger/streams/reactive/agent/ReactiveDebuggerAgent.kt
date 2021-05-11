// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
@file:JvmName("ReactiveDebuggerAgent")

package com.intellij.debugger.streams.reactive.agent

import net.bytebuddy.agent.ByteBuddyAgent
import java.lang.instrument.Instrumentation
import java.util.logging.Logger
import java.util.stream.Stream

/**
 * Entry point for java agent which helps debugging reactive streams.
 * It transforms (via bytecode transformation) chains making
 * possible logging elements values before and after reactive
 * operators.
 *
 * @author Aleksandr Eslikov
 */
@Suppress("unused")
object ReactiveDebuggerAgent {
    private val LOG = Logger.getLogger(ReactiveDebuggerAgent::class.qualifiedName)
    private const val INSTALLED_PROPERTY = "streams.reactive.agent.installed"
    private lateinit var instrumentation: Instrumentation

    /**
     * This method is a part of the Java Agent contract and should not be
     * used directly. Called at JVM startup when agent started
     * using -javaagent parameter
     */
    @Deprecated("to discourage the usage from user's code")
    fun premain(
        args: String?,
        instrumentation: Instrumentation
    ) {
        LOG.info("Agent started from premain")
        instrument(instrumentation)
        System.setProperty(INSTALLED_PROPERTY, "true")
    }

    /**
     * Installs an agent on the currently running Java virtual machine.
     * If an agent cannot be installed, an [IllegalStateException] is thrown.
     */
    @Suppress("unused")
    @Synchronized
    fun init() {
        if (System.getProperty(INSTALLED_PROPERTY) != null) {
            return
        }
        if (ReactiveDebuggerAgent::instrumentation.isInitialized) {
            return
        }
        instrumentation = ByteBuddyAgent.install()
        instrument(instrumentation)
        LOG.info("Stream debugger agent initialized")
    }

    private fun instrument(instrumentation: Instrumentation) {
        val transformer = ClassTransformer()
        instrumentation.addTransformer(transformer, true)
    }

    /**
     * Re-process existing classes
     */
    @Suppress("unused")
    @Synchronized
    fun processExistingClasses() {
        if (System.getProperty(INSTALLED_PROPERTY) != null) {
            return
        }
        if (!ReactiveDebuggerAgent::instrumentation.isInitialized) {
            throw IllegalStateException("Must be initialized first!")
        }
        LOG.info("Class retransformation started")

        try {
            val classes = Stream
                .of(*instrumentation.getInitiatedClasses(ClassLoader.getSystemClassLoader()))
                .filter { shouldRetransformClass(it) }
                .toArray<Class<*>> { length -> arrayOfNulls(length) }

            instrumentation.retransformClasses(*classes)
        } catch (exception: Throwable) {
            LOG.severe("Can't retransform classes. $exception")
            return
        }
        LOG.info("Class retransformation finished")
    }

    private fun shouldRetransformClass(clazz: Class<*>): Boolean {
        try {
            if (!TransformerChecker.shouldTransform(clazz.classLoader, clazz.name, clazz)) {
                return false
            }

            // May trigger NoClassDefFoundError, fail fast
            clazz.constructors
        } catch (linkageError: LinkageError) {
            return false
        }
        return true
    }
}
