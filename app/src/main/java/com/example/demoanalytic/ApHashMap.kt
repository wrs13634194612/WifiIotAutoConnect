package com.example.demoanalytic


class ApHashMap(val size: Int) {
    private data class Entry(val key: String, val value: Any)
    private val array: Array<Entry?>

    init {
        if (size <= 0) {
            throw IllegalArgumentException("HashMap size must be greater than 0")
        }
        this.array = arrayOfNulls(size)
    }

    fun get(key: String): Any? {
        val startIndex = getIndex(key)
        return (0..size-1)
            .map { (startIndex + it) % size }
            .mapNotNull { array[it] }
            .firstOrNull { it.key == key }
            ?.value
    }

    fun set(key: String, value: Any): Boolean {
        val startIndex = getIndex(key)
        val openIndex = (0..size-1)
            .map { (startIndex + it) % size }
            .map { Pair(array[it], it) }
            .firstOrNull { it.first == null || it.first?.key == key }
            ?.second
            ?: return false
        array[openIndex] = Entry(key, value)
        return true
    }

    fun delete(key: String): Any? {
        val startIndex = getIndex(key)
        val deleteEntryIndexPair = (0..size-1)
            .map { (startIndex + it) % size }
            .map { Pair(array[it], it) }
            .firstOrNull { it.first?.key == key }
            ?: return null
        array[deleteEntryIndexPair.second] = null
        return deleteEntryIndexPair.first?.value
    }

    fun load(): Float {
        val numOccupied = array.count { it != null }
        return numOccupied.toFloat() / size
    }

    private fun getIndex(key: String): Int = Math.abs(key.hashCode()) % size
}