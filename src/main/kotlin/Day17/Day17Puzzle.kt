package advent2024.Day17

import advent2024.util.FileReaderUtil
import kotlin.math.pow

class Day17Puzzle {

    var registerA = 0L
    var registerB = 0L
    var registerC = 0L
    val program = mutableListOf<Int>()
    var pointer = 0
    var programOutput = mutableListOf<Int>()



    fun solve(){
        val lines = FileReaderUtil().readFileAsLines("Day17.txt")
        registerA = lines!![0].split(':')[1].trim().toLong()
        registerB = lines[1].split(':')[1].trim().toLong()
        registerC = lines[2].split(':')[1].trim().toLong()
        println(6 xor 35922)
        val programLine =  lines[4].split(':')[1].trim().split(',')
        programLine.forEach { program.add(it.toInt()) }
        println("")
        runProgramPartOne()
        var resultList = ""
        println("Output of program is ${programOutput}" )
        //Reset for Part Two
        resetForPartTwo(0)
        //solvePartTwo()
        //Start off with 0 for part 2
        val partTwo = findRegisterAForProgramOutput(0)
        println("Solved 2: $partTwo")
    }

    fun resetForPartTwo(newAValue: Long) {
        pointer = 0
        registerA = newAValue
        registerB = 0L
        registerC = 0L
        programOutput.clear()

    }

    //Modified DFS
    private fun findRegisterAForProgramOutput(value: Long): Long?{
        //Get a range of value to value + 7
        val rangeOfValues = value..value+8
        //For each value we run through them until we get the first not null value
        val result = rangeOfValues.firstNotNullOfOrNull {  newA ->
            //Reset the registers + output and set register A to new value
            resetForPartTwo(newA)
            //Rerun the program using A
            runProgramPartOne()
            //Get list of the program containing the last elements up to output size and checks to see if matches our output
            if(program.takeLast(programOutput.size) == programOutput){
                if(program == programOutput){
                     newA
                }
                else{
                    //shift bits left, take largest and start again
                    val shift = maxOf(newA shl 3, 8)
                    findRegisterAForProgramOutput(shift)
                }
            }
            else{
                null
            }
        }
        return result
    }


    fun runProgramPartOne(){
        //loop while
        while(pointer < program.size){
            val opcode = program[pointer]
            val operand = program[pointer+1]
            when(opcode){
                0 ->{
                    advanceAInstruction(operand)
                    pointer += 2
                }
                1 -> {
                    bitwiseXORAInstruction(operand)
                    pointer += 2
                }
                2 -> {
                    bitShift(operand)
                    pointer += 2
                }
                3 -> {
                    if(registerA != 0L) jumpInstruction(operand)
                    else pointer += 2
                }
                4 -> {
                    bitwiseXORBInstruction(operand)
                    pointer += 2
                }
                5 -> {
                    outInstruction(operand)
                    pointer +=2
                }
                6 -> {
                    advanceBInstruction(operand)
                    pointer += 2
                }
                7 ->{
                    advanceCInstruction(operand)
                    pointer += 2
                }
            }
        }
    }

    fun getCombo(operand: Int) : Long {
        if(operand in 0 until 4) return operand.toLong()
        if (operand == 4)return registerA
        if (operand == 5) return registerB
        if (operand == 6) return  registerC
        //If we get here we have an error in code
        println("We failed to get a valid combo for $operand")
        return -1
    }

    fun advanceAInstruction(operand: Int) {
       val combo = getCombo(operand)

        registerA /= (2.0.pow(combo.toDouble())).toLong()
    }

    fun bitwiseXORAInstruction(operand: Int){
        registerB = registerB xor operand.toLong()
    }

    fun bitShift(operand: Int) {
        val combo = getCombo(operand)
        registerB = combo % 8

    }

    fun jumpInstruction(operand: Int){
        if(registerA == 0L) return
        pointer = operand
        //Do not increase when calling this
    }

    fun bitwiseXORBInstruction(operand: Int){
        registerB = registerB xor registerC
    }

    fun outInstruction(operand: Int){
        val combo = getCombo(operand)
        val result = combo % 8
        programOutput.add(result.toInt())
    }

    fun advanceBInstruction(operand: Int){
        val combo = getCombo(operand)
        registerB = registerA/(2.0.pow(combo.toDouble())).toLong()
    }

    fun advanceCInstruction(operand: Int){
        val combo = getCombo(operand)
        registerC = registerA/(2.0.pow(combo.toDouble())).toLong()
    }


}