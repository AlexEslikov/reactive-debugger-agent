package com.intellij.debugger.streams.reactive.agent.lib

import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Aleksandr Eslikov
 */
class MethodContext(
    val currentStreamId: String,
    val operationId: AtomicInteger,
    val librarySupport: LibrarySupport
)
