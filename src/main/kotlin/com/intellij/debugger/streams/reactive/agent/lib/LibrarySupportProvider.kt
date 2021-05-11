// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.debugger.streams.reactive.agent.lib

/**
 * This interface should be implemented
 * to instrument and transform additional libraries
 *
 * @author Aleksandr Eslikov
 */
interface LibrarySupportProvider {
    val classFilter: ClassFilter
    val librarySupport: LibrarySupport

    companion object {
        private val providers = mutableSetOf<LibrarySupportProvider>()

        private fun register(instance: LibrarySupportProvider?) {
            if (instance != null) {
                providers.add(instance)
            }
        }

        /**
         * Registers [providers] so they can apply extra transformations
         */
        fun register(providers: List<LibrarySupportProvider>) {
            providers.forEach { register(it) }
        }

        /**
         * Returns registered providers
         */
        fun getProviders(): List<LibrarySupportProvider> {
            return providers.toList()
        }
    }
}
