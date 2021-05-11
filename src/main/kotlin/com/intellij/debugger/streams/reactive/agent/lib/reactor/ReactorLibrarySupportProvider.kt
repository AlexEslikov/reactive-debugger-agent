package com.intellij.debugger.streams.reactive.agent.lib.reactor

import com.intellij.debugger.streams.reactive.agent.lib.LibrarySupportProvider

/**
 * @author Aleksandr Eslikov
 */
class ReactorLibrarySupportProvider : LibrarySupportProvider {
    override val classFilter = ReactorClassFilter()
    override val librarySupport = ReactorLibrarySupport()
}
