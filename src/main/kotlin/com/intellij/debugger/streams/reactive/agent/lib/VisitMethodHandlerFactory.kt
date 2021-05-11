package com.intellij.debugger.streams.reactive.agent.lib

import com.intellij.debugger.streams.reactive.agent.lib.operators.Operator

abstract class VisitMethodHandlerFactory {
    abstract fun getHandler(methodName: String): Operator?
}
