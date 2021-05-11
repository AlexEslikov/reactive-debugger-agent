package com.intellij.debugger.streams.reactive.agent.lib

import com.intellij.debugger.streams.reactive.agent.lib.operators.*
import com.intellij.debugger.streams.reactive.agent.lib.operators.impl.*

/**
 * @author Aleksandr Eslikov
 */
abstract class LibrarySupportBase : LibrarySupport {
    private val intermediateOperators = mutableMapOf<String, Operator>()
    private val publisherOperators = mutableMapOf<String, PublisherOperator>()
    private val terminateOperators = mutableMapOf<String, TerminateOperator>()

    override val visitMethodHandlerFactory: VisitMethodHandlerFactory = object : VisitMethodHandlerFactory() {
        override fun getHandler(methodName: String): Operator? {
            return findOperatorByName(methodName)
        }
    }

    protected fun addIntermediateOperatorsSupport(vararg operations: Operator) {
        operations.forEach { intermediateOperators[it.name] = it }
    }

    protected fun addTerminateOperatorsSupport(vararg operations: TerminateOperator) {
        operations.forEach { terminateOperators[it.name] = it }
    }

    protected fun addPublisherOperatorsSupport(vararg operations: PublisherOperator) {
        operations.forEach { publisherOperators[it.name] = it }
    }

    protected fun simpleOperators(vararg names: String): Array<SimpleOperator> {
        return names.map { SimpleOperator(it) }.toTypedArray()
    }

    protected fun longOperators(vararg names: String): Array<LongOperator> {
        return names.map { LongOperator(it) }.toTypedArray()
    }

    protected fun publisherOperators(vararg names: String): Array<PublisherOperator> {
        return names.map { PublisherOperator(it) }.toTypedArray()
    }

    private fun findOperatorByName(name: String): Operator? {
        return intermediateOperators[name] ?: terminateOperators[name] ?: publisherOperators[name]
    }
}
