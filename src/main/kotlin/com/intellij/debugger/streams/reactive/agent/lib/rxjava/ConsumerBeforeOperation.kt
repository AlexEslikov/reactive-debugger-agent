package com.intellij.debugger.streams.reactive.agent.lib.rxjava

import com.intellij.debugger.streams.reactive.agent.StreamInfoStorage
import io.reactivex.rxjava3.functions.Consumer


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
