package kr.pe.ecmaxp.thumbsj

import kr.pe.ecmaxp.thumbsj.exc.InvalidMemoryException

import java.util.Arrays

class MemoryRegion(val begin: Long, size: Int, val flag: MemoryFlag) {
    val end: Long
    val buffer: ByteArray
    private var Hook: MemoryHook?

    init {
        end = begin + size
        buffer = ByteArray(size)
    }

    constructor(begin: Long, size: Int, hook: MemoryHook) : this(begin, size, MemoryFlag.HOOK) {
        Hook = hook
    }

    override fun toString(): String {
        return "MemoryRegion{" +
                "Begin=" + begin +
                ", End=" + end +
                ", Buffer=" + Arrays.toString(buffer) +
                ", Flag=" + flag +
                '}'.toString()
    }

    @Throws(InvalidMemoryException::class)
    private fun hook(address: Long, read: Boolean, size: Int, value: Int): Int {
        if (Hook == null)
            throw InvalidMemoryException(address)

        return Hook!!.Invoke(address, read, size, value)
    }

    @Throws(InvalidMemoryException::class)
    fun hook(address: Long, size: Int): Int {
        return hook(address, true, size, 0)
    }

    @Throws(InvalidMemoryException::class)
    fun hook(address: Long, size: Int, value: Int) {
        hook(address, false, size, value)
    }
}

