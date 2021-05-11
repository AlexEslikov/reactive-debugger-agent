package com.intellij.debugger.streams.reactive.agent.lib

/**
 * @author Aleksandr Eslikov
 */
interface LibrarySupport {
    val visitMethodHandlerFactory: VisitMethodHandlerFactory
    val methodVisitorFactory: MethodVisitorFactory

    val consumerAfterType: String
    val consumerBeforeType: String
}
