package com.intellij.debugger.streams.reactive.agent

import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Aleksandr Eslikov
 */
internal class StreamInfo(val sourceFileName: String, val streamLines: Set<Int>) {

    private val time = AtomicInteger()
    private val data = mutableMapOf<Int, Value>()

    fun logAfterOperator(operationId: Int, value: Any?, tickOnly: Boolean) {
        time.incrementAndGet()
        if (!tickOnly) {
            val info = data.getOrPut(operationId) { Value() }
            info.after[time.get()] = value
        }
    }

    fun logBeforeOperator(operationId: Int, value: Any?) {
        val info = data.getOrPut(operationId) { Value() }
        info.before[time.get()] = value
    }

    /**
     * Returns result in format accepted by Stream debugger  TraceResultInterpreter
     */
    fun getResult(): Array<Any> {
        return arrayOf(getInfo(), arrayOf<Any?>(null), longArrayOf(0L))
    }

    private fun getInfo(): Array<Any?> {
        val info = arrayOfNulls<Any>(data.size)
        for ((i, value) in data.values.withIndex()) {
            val beforeArray = transformData(value.before)
            val afterArray = transformData(value.after)
            info[i] = arrayOf<Any>(beforeArray, afterArray)
        }
        return info
    }

    private fun transformData(
        map: LinkedHashMap<Int, Any?>
    ): Array<Any?> {
        val keys = IntArray(map.size)
        val values = arrayOfNulls<Any>(map.size)
        for ((i, key) in map.keys.withIndex()) {
            keys[i] = key
            values[i] = map[key]
        }
        return arrayOf(keys, values)
    }


    private class Value {
        val before = LinkedHashMap<Int, Any?>()
        val after = LinkedHashMap<Int, Any?>()
    }

    companion object {
        fun splitStreamIds(streamIds: List<String>): Pair<String, Set<Int>> {
            val streamLines = mutableSetOf<Int>()
            var fileName = ""
            streamIds.forEach { streamId ->
                val parts = streamId.split(":")
                fileName = parts[0]
                val lineNumber = parts[1].toInt()
                streamLines.add(lineNumber)
            }
            return Pair(fileName, streamLines)
        }

        fun splitStreamId(streamId: String): Pair<String, Int> {
            val parts = streamId.split(":")
            return parts[0] to parts[1].toInt()
        }
    }
}
