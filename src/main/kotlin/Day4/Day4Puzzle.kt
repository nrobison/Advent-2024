package advent2024.Day4

import advent2024.util.FileReaderUtil

class Day4Puzzle {

    fun solvePart1(): Int {
        val fileReader = FileReaderUtil()
        val text = fileReader.readFileAsLines("Day4.txt")
        val arrayOfTexts = fileReader.convertLinesToArrayOfChars(text!!)
        val numberOfFinds = findAllXMAS(arrayOfTexts)
        return numberOfFinds
    }

    fun findAllXMAS(grid: Array<CharArray>): Int {
        val rows = grid.size
        val cols = grid[0].size
        val occurrences = mutableListOf<List<Pair<Int, Int>>>()

        fun checkDirection(row: Int, col: Int, dr: Int, dc: Int) {
            var r = row
            var c = col
            var word = ""
            val order = mutableListOf<Pair<Int, Int>>()
            for (i in 0..3) {
                if (r in 0 until rows && c in 0 until cols) {
                    order.add(Pair(r, c))
                    word += grid[r][c]
                    r += dr
                    c += dc
                } else {
                    return
                }
            }
            if (word == "XMAS" || word == "SAMX") {
                if (!occurrences.contains(order.reversed()) && !occurrences.contains(order)) {
                    //Add occurance if it's not a backwards of one already found
                    occurrences.add(order)
                }

            }

        }

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                // Check all 8 directions
                checkDirection(row, col, 1, 0)
                checkDirection(row, col, -1, 0)
                checkDirection(row, col, 0, 1)
                checkDirection(row, col, 0, -1)
                checkDirection(row, col, 1, 1)
                checkDirection(row, col, 1, -1)
                checkDirection(row, col, -1, 1)
                checkDirection(row, col, -1, -1)
            }
        }
        return occurrences.size
    }

    fun solvePart2() : Int {
        val fileReader = FileReaderUtil()
        val text = fileReader.readFileAsLines("Day4.txt")
        val arrayOfTexts = fileReader.convertLinesToArrayOfChars(text!!)
        val numberOfFinds = findAllXMASWithACentered(arrayOfTexts)
        return numberOfFinds
    }

    fun findAllXMASWithACentered(grid: Array<CharArray>): Int {
        val rows = grid.size
        val cols = grid[0].size
        val occurrences = mutableListOf<List<Pair<Int, Int>>>()

        fun checkDirection(row: Int, col: Int, dr: Int, dc: Int) {
            var r = row
            var c = col
            var word = ""
            val order = mutableListOf<Pair<Int, Int>>()
            for (i in 0..2) {
                if (r in 0 until rows && c in 0 until cols) {
                    order.add(Pair(r, c))
                    word += grid[r][c]
                    r += dr
                    c += dc
                } else {
                    return
                }
            }
            if (word == "MAS" || word == "SAM") {
                if (!occurrences.contains(order.reversed()) && !occurrences.contains(order)) {
                    //Add occurance if it's not a backwards of one already found
                    occurrences.add(order)
                }

            }

        }

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                checkDirection(row, col, 1, 1)
                checkDirection(row, col, 1, -1)
                checkDirection(row, col, -1, 1)
                checkDirection(row, col, -1, -1)
            }
        }
        var total = 0
        val validPairs = mutableListOf<List<Pair<Int, Int>>>()
        //We go through the list of MAS or SAM and then compare the A position
        for(items in occurrences){
            val tempCopy = occurrences.toMutableList()
            //We don't want accidentally match with the same item
            tempCopy.remove(items)
            val item_pair = tempCopy.find { pairs -> pairs.any { pair -> pair == items[1]} }
            if(item_pair != null && !validPairs.contains(items) && !validPairs.contains(item_pair)){
                validPairs.add(items)
                validPairs.add(item_pair)
                total += 1
            }
        }

        return total
    }
}