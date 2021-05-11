package com.intellij.debugger.streams.reactive.agent.lib.reactor

import com.intellij.debugger.streams.reactive.agent.StreamInfoStorage
import java.util.function.Consumer

/**
 * @author Aleksandr Eslikov
 */
@Suppress("unused")
class ConsumerBeforeOperation(
    private val streamId: String,
    private val operationId: Int
) : Consumer<Any?> {
    override fun accept(value: Any?) {
        StreamInfoStorage.logBeforeOperator(streamId, operationId, value)
    }
}
