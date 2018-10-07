package kr.pe.ecmaxp.openpie.console

import kr.pe.ecmaxp.thumbsf.CPU
import kr.pe.ecmaxp.thumbsf.MemoryFlag
import kr.pe.ecmaxp.thumbsf.consts.PC
import kr.pe.ecmaxp.thumbsf.signal.ControlStopSignal
import java.io.File
import java.nio.file.Files
import java.time.Instant


object OpenPieMain {
    internal var pos = 0

    @JvmStatic
    fun main(args: Array<String>) {
        val cpu = CPU()
        val file = File("C:\\Users\\EcmaXp\\Dropbox\\Projects\\openpie\\oprom\\build\\firmware.bin")
        val firmware = Files.readAllBytes(file.toPath())
        val KB = 1024
        val memory = cpu.memory
        memory.flash(0x08000000, 256 * KB, firmware) // flash
        memory.map(0x20000000, 64 * KB, MemoryFlag.RW) // sram
        memory.map(0x60000000, 192 * KB, MemoryFlag.RW) // ram
        memory.map(-0x20000000, 16 * KB, MemoryFlag.RW) // syscall

        cpu.regs[PC] = memory.readInt(0x08000000 + 4)
        val newCPU = cpu.copy()

        if (true) {
            val start = Instant.now()

            try {
                while (true)
                    newCPU.run(10000) {
                        throw ControlStopSignal()
                    }
            } catch (e: ControlStopSignal) {
                val end = Instant.now()
                println(end.toEpochMilli() - start.toEpochMilli())
            }
        }

        if (true) {
            val start = Instant.now()

            try {
                cpu.run(Int.MAX_VALUE) {
                    throw ControlStopSignal()
                }
            } catch (e: ControlStopSignal) {
                val end = Instant.now()
                println(end.toEpochMilli() - start.toEpochMilli())
            }
        }
    }
}
