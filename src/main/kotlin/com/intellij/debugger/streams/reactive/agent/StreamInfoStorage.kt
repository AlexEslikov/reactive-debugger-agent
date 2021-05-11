// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.debugger.streams.reactive.agent

import com.intellij.debugger.streams.reactive.agent.StreamInfo.Companion.splitStreamId
import com.intellij.debugger.streams.reactive.agent.StreamInfo.Companion.splitStreamIds

/**
 * Provides access to all recorded stream data
 *
 * @author Aleksandr Eslikov
 */
object StreamInfoStorage {
    private val streams = mutableSetOf<StreamInfo>()
    private val enableFlags = mutableSetOf<String>()

    /**
     * Return recorded data for [streamId] in format accepted by Stream debugger
     */
    @Suppress("unused")
    fun getResult(streamId: String): Array<Any>? {
        return getStream(streamId)?.getResult()
    }

    private fun getStream(streamId: String): StreamInfo? {
        val (fileName, lineNumber) = splitStreamId(streamId)
        return streams.firstOrNull { it.sourceFileName == fileName && it.streamLines.contains(lineNumber) }
    }

    @Suppress("unused")
    @Synchronized
    fun enableLogForStream(streamId: String) {
        enableLogForStream(listOf(streamId))
    }

    @Synchronized
    fun enableLogForStream(streamIds: List<String>) {
        val (fileName, streamLines) = splitStreamIds(streamIds)
        if (isLoggingEnabled(streamIds)) {
            return
        }
        val streamInfo = StreamInfo(fileName, streamLines)
        streams.add(streamInfo)
        enableFlags.addAll(streamIds)
    }

    @Suppress("unused")
    @Synchronized
    fun disableLogForStream(streamId: String) {
        disableLogForStream(listOf(streamId))
    }

    @Synchronized
    fun disableLogForStream(streamIds: List<String>) {
        val (fileName, streamLines) = splitStreamIds(streamIds)
        streams.removeIf { streamInfo ->
            streamInfo.sourceFileName == fileName && streamLines.any { streamInfo.streamLines.contains(it) }
        }
        enableFlags.removeAll(streamIds)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun isLoggingEnabled(streamIds: List<String>): Boolean {
        return streamIds.any { enableFlags.contains(it) }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun isLoggingEnabled(streamId: String): Boolean {
        return enableFlags.contains(streamId)
    }

    internal fun logBeforeOperator(streamId: String, operationId: Int, value: Any?) {
        if (!isLoggingEnabled(streamId)) {
            return
        }
        getStream(streamId)?.logBeforeOperator(operationId, value)
    }

    internal fun logAfterOperator(streamId: String, operationId: Int, value: Any?, tickOnly: Boolean) {
        if (!isLoggingEnabled(streamId)) {
            return
        }
        getStream(streamId)?.logAfterOperator(operationId, value, tickOnly)
    }
}

