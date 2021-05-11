package com.intellij.debugger.streams.reactive.agent.lib.reactor

import com.intellij.debugger.streams.reactive.agent.DebugAgentConstants
import com.intellij.debugger.streams.reactive.agent.lib.LibrarySupportBase
import com.intellij.debugger.streams.reactive.agent.lib.operators.impl.TerminateOperator
import com.intellij.debugger.streams.reactive.agent.lib.rxjava.ConsumerAfterOperation
import com.intellij.debugger.streams.reactive.agent.lib.rxjava.ConsumerBeforeOperation

/**
 * @author Aleksandr Eslikov
 */
class ReactorLibrarySupport : LibrarySupportBase() {
    init {
        addIntermediateOperatorsSupport(
            *simpleOperators(
                "map",
                "filter"
            )
        )
        addIntermediateOperatorsSupport(
            *longOperators(
                "take"
            )
        )
        addTerminateOperatorsSupport(TerminateOperator("subscribe"))

        addPublisherOperatorsSupport(
            *publisherOperators(
                "just",
                "interval"
            )
        )
    }

    override val methodVisitorFactory = ReactorMethodVisitorFactory()
    override val consumerAfterType: String =
        "${DebugAgentConstants.PACKAGE_INTERNAL}/lib/reactor/${ConsumerAfterOperation::class.simpleName}"
    override val consumerBeforeType: String =
        "${DebugAgentConstants.PACKAGE_INTERNAL}/lib/reactor/${ConsumerBeforeOperation::class.simpleName}"
}
