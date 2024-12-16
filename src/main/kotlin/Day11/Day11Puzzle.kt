package advent2024.Day11

import advent2024.util.FileReaderUtil

class Day11Puzzle {

    //Brute Force not even close to efficient for 75, Part 2 uses DFS with memorization
    fun solve(numberOfBlinks: Int) {
        val fileReader = FileReaderUtil()
        val numbers = fileReader.convertLineToListOfLongs(fileReader.readFileAsText("Day11.txt")).toMutableList()
        for(index in 0 until numberOfBlinks){
            var number = 0
            while(number < numbers.size){
                val originalNumber = numbers[number]
                //If 0 change it to 1
                if(originalNumber == 0L){
                    numbers[number] = 1
                }
                //If number is even number of digits split it
                else if(originalNumber.toString().length % 2 == 0 ){
                    val midPointOfTest = originalNumber.toString().length /2
                    numbers[number] = originalNumber.toString().substring(0,midPointOfTest).toLong()
                    numbers.add(number+1,originalNumber.toString().substring(midPointOfTest).toLong())
                    number++ //Increase it by 1 so we don't check the newly split number
                }
                //Else we multiply the odd digit by 2024
                else {
                    numbers[number] = originalNumber * 2024
                }
                number++
            }

        }
        println("Number of stones = ${numbers.size}")
    }

    //Need to be more efficient need to use a DFS with memorization - Super fast
    fun solvePart2(steps: Int) {
        val blinkedStones = mutableMapOf<Pair<Long,Int>, Long>()

        //Nested function why not, allows us to keep reference to stones
        fun traverseStones(stone: Long, steps: Int): Long {
            var result = 0L
            //First Rule is if the stone is 0 it turns to 1 no need to go further and expand
            if(steps == 0) return 1
            //Check if we already have it, if so don't repeat return it
            if(blinkedStones.containsKey(Pair(stone,steps))) return blinkedStones[Pair(stone,steps)]!!
            val numberString = stone.toString()
            //If stone is 0 turn it to 1 and keep going down dfs
            if(stone == 0L) {
                result = traverseStones(1, steps - 1)
            }
            //If even number of digits split it
            else if(numberString.length % 2 == 0){
                result = traverseStones(numberString.substring(0,numberString.length /2).toLong(),steps -1)
                result += traverseStones(numberString.substring(numberString.length /2,).toLong(),steps -1)
            }
            //Else we multiply the number by 2024
            else {
                result = traverseStones(stone * 2024, steps -1)
            }
            blinkedStones[Pair(stone,steps)] = result
            return result
        }
        val fileReader = FileReaderUtil()
        val numbers = fileReader.convertLineToListOfLongs(fileReader.readFileAsText("Day11.txt")).toMutableList()
        val results = numbers.sumOf{stone -> traverseStones(stone,steps)}
        println("Number of stones for $steps : $results")
    }
}