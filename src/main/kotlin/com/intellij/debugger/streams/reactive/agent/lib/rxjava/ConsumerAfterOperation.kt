package com.intellij.debugger.streams.reactive.agent.lib.rxjava

import com.intellij.debugger.streams.reactive.agent.StreamInfoStorage
import io.reactivex.rxjava3.functions.Consumer

@Suppress("unused")
class ConsumerAfterOperation(
    private val streamId: String,
    private val operationId: Int,
    private val tickOnly: Boolean
) :
    Consumer<Any?> {
    override fun accept(value: Any?) {
        StreamInfoStorage.logAfterOperator(streamId, operationId, value, tickOnly)
    }
}
