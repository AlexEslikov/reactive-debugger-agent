package com.intellij.debugger.streams.reactive.agent.lib.rxjava

import com.intellij.debugger.streams.reactive.agent.lib.LibrarySupportProvider

/**
 * @author Aleksandr Eslikov
 */
class RxJavaLibrarySupportProvider : LibrarySupportProvider {
    override val classFilter = RxJavaClassFilter()
    override val librarySupport = RxJavaLibrarySupport()
}
