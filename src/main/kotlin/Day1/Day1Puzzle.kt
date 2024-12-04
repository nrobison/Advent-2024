package advent2024.Day1

import advent2024.util.FileReaderUtil
import kotlin.math.abs

class Day1Puzzle {

    val fileReaderUtil = FileReaderUtil()
    fun solvePart1() : Int{
        val input = fileReaderUtil.readFileAsLines("Day1.txt") ?: emptyList()
        var totals = 0
        val leftListOfNumbers = mutableListOf<Int>()
        val rightListOfNumbers = mutableListOf<Int>()
        for(line in input){
            val convertedLineToNumber = fileReaderUtil.convertLineToListOfNumbers(line)
            leftListOfNumbers.addLast(convertedLineToNumber[0])
            rightListOfNumbers.addLast(convertedLineToNumber[1])
        }
        leftListOfNumbers.sortDescending()
        rightListOfNumbers.sortDescending()
        for(index in leftListOfNumbers.indices){
            totals += abs(leftListOfNumbers[index] - rightListOfNumbers[index])
        }
        return totals
    }

    fun solvePart2(): Int{
        val input = fileReaderUtil.readFileAsLines("Day1.txt") ?: emptyList()
        var totals = 0
        val leftListOfNumbers = mutableListOf<Int>()
        val rightListOfNumbers = mutableListOf<Int>()
        for(line in input){
            val convertedLineToNumber = fileReaderUtil.convertLineToListOfNumbers(line)
            leftListOfNumbers.addLast(convertedLineToNumber[0])
            rightListOfNumbers.addLast(convertedLineToNumber[1])
        }
        for(index in leftListOfNumbers.indices) {
            totals += (leftListOfNumbers[index] * rightListOfNumbers.count{it == leftListOfNumbers[index]})
        }
        return totals
    }
}