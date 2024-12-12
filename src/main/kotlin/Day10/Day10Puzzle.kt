package advent2024.Day10

import advent2024.util.FileReaderUtil

class Day10Puzzle {

    var mapToIntArray = Array(0) { IntArray(0)}
    val validPaths = mutableListOf<Pair<Pair<Int,Int>,Pair<Int,Int>>>()
    fun solve(isPart2: Boolean){
        val fileReader = FileReaderUtil().readFileAsLines("Day10.txt")
        mapToIntArray = fileReader!!.map { str -> str.map { it.digitToInt() }.toIntArray() }.toTypedArray()
        findValidPaths(isPart2)
        println("Total of Trailheads: ${validPaths.size}")
        clear()
    }

    fun clear(){
        mapToIntArray = Array(0) { IntArray(0)}
        validPaths.clear()
    }

    fun findValidPaths(isPart2: Boolean) {
        for (i in mapToIntArray.indices) {
            for (j in mapToIntArray[0].indices) {
                if (mapToIntArray[i][j] == 0) { // Start depthFirstPathSearch from any map point with value 0
                    depthFirstPathSearch(i, j, 0, null, mutableListOf(),isPart2)
                }
            }
        }
        return
    }

    fun isValid(row: Int, col: Int): Boolean {
        return row in mapToIntArray.indices && col in mapToIntArray[0].indices
    }

    fun depthFirstPathSearch(row: Int, col: Int, currentValue: Int, previousDirection: Pair<Int, Int>?, currentPath: MutableList<Pair<Int, Int>>, isPart2: Boolean){
        if (!isValid(row, col) || mapToIntArray[row][col] != currentValue) {
            return
        }

        currentPath.add(Pair(row, col))

        if (currentValue == 9 && isPart2 ){
            validPaths.add(Pair(currentPath.first(),currentPath.last()))
            //   printPath(grid[0].size, grid.size, currentPath)
            currentPath.removeLast()
            return
        }
        //Part 1 can't have multiple points with same start and end point.
        else if(currentValue == 9 && !validPaths.contains(Pair(currentPath.first(),currentPath.last()))) {
            validPaths.add(Pair(currentPath.first(),currentPath.last()))
            //   printPath(grid[0].size, grid.size, currentPath)
            currentPath.removeLast()
            return
        }

        mapToIntArray[row][col] = -1 // Mark as visited

        val directions = listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))

        for (direction in directions) {
            if (previousDirection != null && direction == Pair(-previousDirection.first, -previousDirection.second)) {
                continue
            }
            depthFirstPathSearch(row + direction.first, col + direction.second, currentValue + 1, direction, currentPath, isPart2)
        }

        mapToIntArray[row][col] = currentValue // Restore original value
        currentPath.removeLast()
    }

    fun printPath(column :Int , row: Int, paths: MutableList<Pair<Int,Int>> ) {
        for(r in 0 until row ){
            var string = ""
            val count = 0
            for(c in 0 until column){
                if(paths.contains(Pair(r,c))){
                    string += paths.indexOf(Pair(r,c))
                }
                else{
                    string += "."
                }
            }
            println(string)
        }
        println("")
        println("-----------")
        println("")
    }
}