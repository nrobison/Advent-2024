package advent2024.Day18

import advent2024.util.FileReaderUtil
import java.util.*

class Day18Puzzle {

    fun solve() {
        val input = FileReaderUtil().readFileAsLines("Day18.txt")!!
        val bytePoints = mutableListOf<Pair<Int,Int>>()
        input.forEach {
            val point = it.split(',')
            bytePoints.add(Pair(point[0].toInt(), point[1].toInt()))
        }
        //70,70 for full or 6,6 for test
        val endPoint = Pair(70, 70)
        val bytes = 1024
        var path = findPath(bytePoints,endPoint,bytes)
        //printPath(items,endPoint,bytePoints.subList(0,12))
        println("Shortest Path Part 1: ${path.size-1}")
        //This is not efficient but works. It eventually finds it. We could probably do some math
        //to find the point faster
        for(byte in bytes until bytePoints.size ){
            path = findPath(bytePoints,endPoint,byte)
            if(path.isEmpty()) {
                println("We found the first one that breaks the path ${bytePoints[byte-1]} ")
                break
            }
        }

    }

    //BFS
    private fun findPath(bytePoints: List<Pair<Int,Int>>, endPoint: Pair<Int,Int>, numberOfBytes: Int): List<Pair<Int, Int>> {
        val startPoint = Pair(0, 0)
        val blocks = bytePoints.subList(0, numberOfBytes)
        val visited = mutableListOf<Pair<Int, Int>>()
        val queue: Queue<Pair<Int, Int>> = LinkedList()
        val parentMap = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        //var shortestPath: List<Pair<Int,Int>>? = null
        //var shortestPathLength = Int.MAX_VALUE
        val directions = arrayOf(
            Pair(-1, 0), // Up
            Pair(1, 0),  // Down
            Pair(0, -1), // Left
            Pair(0, 1)   // Right
        )
        queue.offer(startPoint)
        visited.add(startPoint)
        while (queue.isNotEmpty()) {
            val (row, col) = queue.poll()

            if (row == endPoint.first && col == endPoint.second) {
                return reconstructPath(parentMap, endPoint)
            }

            for ((dr, dc) in directions) {
                val newRow = row + dr
                val newCol = col + dc
                val newPos = Pair(newRow, newCol)

                if (newRow in 0.. endPoint.second && newCol in 0..endPoint.first &&
                    !blocks.contains(newPos) && newPos !in visited
                ) {
                    visited.add(newPos)
                    parentMap[newPos] = Pair(row, col)
                    queue.offer(newPos)
                }
            }
        }

        return emptyList()
    }

    fun reconstructPath(parentMap: Map<Pair<Int,Int>, Pair<Int,Int>>, end: Pair<Int,Int>): List<Pair<Int,Int>> {
        val path = mutableListOf<Pair<Int,Int>>()
        var current = end
        while (current in parentMap) {
            path.add(0, current)
            current = parentMap[current]!!
        }
        path.add(0, current) // Add the start node
        return path
    }

    private fun printPath(path: List<Pair<Int,Int>>, endPoint: Pair<Int, Int>, blocks: List<Pair<Int,Int>> ){
        for(y in 0..endPoint.second){
            var line = ""
            for(x in 0..endPoint.first){
                val tempPoint = Pair(x,y)
                if(blocks.contains(tempPoint)) line+= "#"
                else if(path.contains(tempPoint)) line+= "0"
                else line+= "."
                if(path.contains(tempPoint) && blocks.contains(tempPoint)){
                    println("Point is in both blocks and path error $tempPoint")
                }
            }
            println(line)
        }
    }

//        fun dfs(current: Pair<Int,Int>, currentPath: List<Pair<Int,Int>>) {
//            if (current == endPoint) {
//                if (currentPath.size < shortestPathLength) {
//                    shortestPathLength = currentPath.size
//                    shortestPath = currentPath.toList() // Create a copy to avoid modification
//                }
//                return
//            }
//
//            visited.add(current)
//
//            val directions = listOf(Point(1, 0), Point(-1, 0), Point(0, 1), Point(0, -1))
//            for (dir in directions) {
//                val next = Pair(current.first + dir.x, current.second + dir.y)
//                if (next.first in 0 until endPoint.second+1 && next.second in 0 until endPoint.first+1 &&
//                    !visited.contains(next) &&  next.first in 0..endPoint.first && next.second in 0..endPoint.second && !blocks.contains(next) &&
//                    currentPath.size + 1 < shortestPathLength // Optimization
//                ) {
//                    dfs(next, currentPath + next)
//                }
//            }
//
//            visited.remove(current)
//        }
//
//        dfs(startPoint, listOf(startPoint))
//        for(y in 0..endPoint.second){
//            var line = ""
//            for(x in 0..endPoint.first){
//                val tempPoint = Pair(x,y)
//                if(blocks.contains(tempPoint)) line+= "#"
//                else if(shortestPath!!.contains(tempPoint)) line+= "0"
//                else line+= "."
//                if(shortestPath!!.contains(tempPoint) && blocks.contains(tempPoint)){
//                    println("Point is in both blocks and path error $tempPoint")
//                }
//            }
//            println(line)
//        }
//        println("Found path: Shortest Distance: ${shortestPath!!.size}")

//    }
}