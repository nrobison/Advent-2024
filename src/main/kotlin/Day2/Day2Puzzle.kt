package advent2024.day2Puzzle

import advent2024.util.FileReaderUtil
import kotlin.math.abs


class Day2Puzzle {

enum class LineDirection{
    NONE,
    INCREASE,
    DECREASE
}
    val fileReaderUtil = FileReaderUtil()
    fun solvePart1() : Int{
        val input = fileReaderUtil.readFileAsLines("Day2.txt") ?: emptyList()
        var numberOfSafeRows = 0
        for(line in input){
            val numbers = fileReaderUtil.convertLineToListOfNumbers(line)
            if(checkIfLineIsValid(numbers)) numberOfSafeRows += 1
        }
        return numberOfSafeRows
    }

    fun solvePart2() : Int{
        val input = FileReaderUtil().readFileAsLines("Day2.txt") ?: emptyList()
        var numberOfSafeRows = 0
        for(line in input) {
            val numbers = fileReaderUtil.convertLineToListOfNumbers(line)
            if (checkIfLineIsValid(numbers)) numberOfSafeRows += 1
            //Got to have at least 3 elements to remove one and check
            else if(numbers.size > 2){
                for( index in numbers.indices){
                    val holdList = numbers.toMutableList()
                    holdList.removeAt(index)
                    if(checkIfLineIsValid(holdList)){
                        numberOfSafeRows += 1
                        break
                    }
                }

            }
        }
        return numberOfSafeRows
    }

    private fun checkIfLineIsValid(lineOfNumbers: List<Int>) : Boolean{
        var currentNumber =0
        var currentDirection = LineDirection.NONE
        for(index in lineOfNumbers.indices){
            if(index == lineOfNumbers.size-1) {
                return false
            }
            currentNumber = lineOfNumbers[index]
            val nextNumber = lineOfNumbers[index + 1]
            val newDirection = checkIncreaseOrDecrease(currentNumber,nextNumber)
            //println("Current number $currentNumber || current index $index || next number $nextNumber || new direction $newDirection")
            if(currentDirection == LineDirection.NONE) currentDirection = newDirection
            else if(newDirection != currentDirection) {
                return false
            }
            //Check if 1 or 2 difference
            val differenceCheck = abs(currentNumber - nextNumber)
            if (differenceCheck >= 4  || differenceCheck == 0 ) {
                return false
            }
            if(index+1 == lineOfNumbers.size-1) {
                return true
            }
        }
        //Fail-safe?
        return false
    }


    private fun checkIncreaseOrDecrease(firstNumber: Int, secondNumber: Int) : LineDirection{
        //If the first number is larger than the second we are decreasing
        if(firstNumber > secondNumber) return LineDirection.DECREASE
        return LineDirection.INCREASE
    }
}