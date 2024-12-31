package advent2024.Day24

import advent2024.util.FileReaderUtil

class Day24Puzzle {

    enum class Gate(val input: String){
        XOR("XOR"),
        OR("OR"),
        AND("AND");

        companion object {
            fun fromString(value: String): Gate? {
                return values().find { it.name == value.uppercase() }
            }
        }
    }

    data class GateInput(val leftInput: String, val rightInput: String, val targetWire: String, val operation: Gate?)

    fun solve() {

        val input = FileReaderUtil().readFileAsLines("Day24.txt")!!
        val wireInput = mutableMapOf<String,Int>()
        val gateInputs = mutableListOf<GateInput>()
        input.forEach{
            if(it.contains(":")){
                val wire = it.split(":")
                wireInput.putIfAbsent(wire[0],wire[1].trim().toInt())
            }
            else if(it.contains("->")){
                val gateInput = it.split(" ")
                gateInputs.add(GateInput(gateInput[0],gateInput[2], gateInput[4], Gate.fromString(gateInput[1])))
            }
        }
        println()
        processGateInputs(wireInput,gateInputs)
    }

    fun processGateInputs(wireInputs: Map<String, Int>, gateInputs: List<GateInput> ) {
        val unfinishedInputs = gateInputs.toMutableList()
        var currentInputs = gateInputs.toList()
        val wires = wireInputs.toMutableMap()
        while(currentInputs.isNotEmpty()){
                currentInputs.forEach { input ->
                    if(wires.containsKey(input.leftInput) && wires.containsKey(input.rightInput)){
                        if(input.operation == Gate.AND) wires.put(input.targetWire,(wires[input.leftInput]?.and(wires[input.rightInput]!!)!!))
                        if(input.operation == Gate.OR) wires.put(input.targetWire,(wires[input.leftInput]?.or(wires[input.rightInput]!!)!!))
                        if(input.operation == Gate.XOR) wires.put(input.targetWire,(wires[input.leftInput]?.xor(wires[input.rightInput]!!)!!))
                        unfinishedInputs.remove(input)
                    }
                }
            currentInputs = unfinishedInputs.toList()
        }
        val zKeys = wires.keys.filter { it.contains('z') }.sorted()
        var bitString = ""
        zKeys.forEach { it -> bitString = wires[it].toString() + bitString }
        println(bitString.toLong(radix=2))
    }
}