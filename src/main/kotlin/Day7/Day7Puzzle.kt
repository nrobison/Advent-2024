package advent2024.Day7

import advent2024.util.FileReaderUtil
import java.math.BigInteger

class Day7Puzzle {

    private val linesOfInput = FileReaderUtil().readFileAsLines("Day7.txt")
    private val mappedInput = linesOfInput!!.map { line ->
        val split = line.split(":")
        //Left is total
        val total = split[0].trim().toLong()
        //Right numbers
        val numbers = split[1].trim().split("\\s".toRegex()).map { it.toLong()}.toLongArray()
        Pair(total,numbers)
    }

    fun solvePart1(): Long {
        var totalOfValidInputs = 0L
        for (rows in mappedInput) {
            //Recursively solve it.
            if (addAndMultiply(rows.first.toLong(), rows.second, 1, rows.second[0])) {
                totalOfValidInputs += rows.first
            }
        }
            return totalOfValidInputs
    }

    fun solvePart2(): Long{
        var totalOfValidInputs = 0L
        for( rows in mappedInput) {
            //Recursively solve it.
            if(AddMultiplyConcat(rows.first.toLong(), rows.second,1,rows.second[0])) {
                    totalOfValidInputs += rows.first
                   // println("P2 Total: $totalOfValidInputs -> Added recently ${rows.first} consisting of ${rows.second}")
            }
        }
        return totalOfValidInputs
    }

    private fun addAndMultiply(targetTotal: Long, numbers: LongArray, currentIndex: Int, currentTotal: Long) :Boolean {
        //If we reached the end break out of recursion and return boolean of if currentTotal = our target
        if (currentIndex == numbers.size) {
            return currentTotal == targetTotal
        }
        if(currentTotal > targetTotal)return false
        val number = numbers[currentIndex]
        //Go down Both paths of Add or Multiply. This covers all permutations
        return addAndMultiply(targetTotal, numbers, currentIndex + 1, currentTotal + number) ||
                addAndMultiply(targetTotal, numbers, currentIndex + 1, currentTotal * number)
    }

    fun AddMultiplyConcat(targetTotal: Long, numbers: LongArray, currentIndex: Int, currentTotal: Long): Boolean {
        if (currentIndex == numbers.size) {
            return currentTotal == targetTotal
        }
        if(currentTotal > targetTotal)return false
        val number = numbers[currentIndex]

        return AddMultiplyConcat(targetTotal,numbers,currentIndex + 1, currentTotal + number) ||
                AddMultiplyConcat(targetTotal,numbers,currentIndex + 1, currentTotal * number) ||
                AddMultiplyConcat(targetTotal,numbers,currentIndex + 1, "$currentTotal$number".toLong())
    }

}

