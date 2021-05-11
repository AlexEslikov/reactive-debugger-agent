package com.intellij.debugger.streams.reactive.agent.lib.rxjava

import com.intellij.debugger.streams.reactive.agent.DebugAgentConstants
import com.intellij.debugger.streams.reactive.agent.lib.LibrarySupportBase
import com.intellij.debugger.streams.reactive.agent.lib.operators.impl.TerminateOperator

/**
 * @author Aleksandr Eslikov
 */
class RxJavaLibrarySupport : LibrarySupportBase() {
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

    override val methodVisitorFactory = RxJavaMethodVisitorFactory()
    override val consumerAfterType: String = "${DebugAgentConstants.PACKAGE_INTERNAL}/lib/rxjava/${ConsumerAfterOperation::class.simpleName}"
    override val consumerBeforeType: String =  "${DebugAgentConstants.PACKAGE_INTERNAL}/lib/rxjava/${ConsumerBeforeOperation::class.simpleName}"
}
