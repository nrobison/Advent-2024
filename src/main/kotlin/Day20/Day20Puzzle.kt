package advent2024.Day20

import advent2024.util.FileReaderUtil
import advent2024.util.MapMarker
import advent2024.util.Point
import advent2024.util.RacePoint
import kotlin.math.abs


class Day20Puzzle {


    var raceMap = mapOf<Point, RacePoint>()
    val visitedPoints = mutableListOf<Point>()
    val cheatsCounted = mutableMapOf<Int, Int>()


    data class RaceState(
        var currentPoint: Point,
        var start: Point,
        var end: Point,
        var usedCheatOne: Point?,
        var usedCheatTwo: Point?
    )

    //var raceMap = mapOf<Point, RacePoint>()
    fun solve() {
        val linesOfInput = FileReaderUtil().readFileAsLines("Day20.txt")
        raceMap = linesOfInput!!.flatMapIndexed { yIndex, row ->
            row.toCharArray().mapIndexed { xIndex, char ->
                Point(xIndex, yIndex) to RacePoint(char)
            }
        }.toMap().withDefault { RacePoint(' ') }
        val start = raceMap.entries.find { it.value.type == MapMarker.START }!!.key
        val end = raceMap.entries.find { it.value.type == MapMarker.END }!!.key
        getPathResults(start, end)
        //printallPaths(visitedPoints)
        findAllCheatPaths(2)
        findCheatPathsManhattan(2,2)
        println(
            "Number of 2 space cheats (part 1): ${
                cheatsCounted.toList().sumOf { it.second }
            }"
        )
        cheatsCounted.clear()
        findCheatPathsManhattan(20, 100)
        println(
            "Number of max of 20 space cheats with minimum distance of 100 (part 2): ${
                cheatsCounted.toList().sumOf { it.second }
            }"
        )


    }

    fun manhattanDistance(p1: Point, p2: Point): Int {
        return abs(p1.x - p2.x) + abs(p1.y - p2.y)
    }


    private fun findAllCheatPaths(maxSpaceSkip: Int) {
        visitedPoints.forEach { it ->
            val neighbors = findAllValidCheats(it, maxSpaceSkip)
            neighbors.forEach { neighbor ->
                val previousIndex = visitedPoints.indexOf(it)
                val nextIndex = visitedPoints.indexOf(neighbor)
                if (previousIndex < nextIndex) {
                    val cheatSaving =
                        visitedPoints.subList(visitedPoints.indexOf(it) + 1, visitedPoints.indexOf(neighbor) - 1)
                    if (cheatSaving.size != 0) {
                        cheatsCounted[cheatSaving.size] = cheatsCounted.getOrPut(cheatSaving.size, { 0 }) + 1
                    }
                }
            }
        }
    }

    fun findCheatPathsManhattan(maxSpace: Int, minDistance: Int) {
        for (index in visitedPoints.indices) {
            val range = (index + minDistance + 2)..visitedPoints.size
            range.forEach { subItem ->
                if (subItem < visitedPoints.size) {
                    val distance = manhattanDistance(visitedPoints[index], visitedPoints[subItem])
                    if (distance in 2..maxSpace) {
                        val cheatSaving = subItem - index - distance
                        if (cheatSaving >= minDistance)
                            cheatsCounted[cheatSaving] = cheatsCounted.getOrPut(cheatSaving, { 0 }) + 1

                    }
                }
            }
        }
    }


    private fun findAllValidCheats(currentPoint: Point, maxSpaceSkip: Int): List<Point> {
        val neighbors = mutableListOf<Point>()
        for (numberOfSkips in 1 until maxSpaceSkip) {
            val directions = arrayOf(
                intArrayOf(-(1 + numberOfSkips), 0),
                intArrayOf((1 + numberOfSkips), 0),
                intArrayOf(0, -(1 + numberOfSkips)),
                intArrayOf(0, (1 + numberOfSkips))
            )
            for (direction in directions) {
                val neighbor = Point(currentPoint.x + direction[0], currentPoint.y + direction[1])
                //If it's not a wall and we haven't visited (prevent going backward)
                val type = raceMap[neighbor]
                if (raceMap[neighbor] == null) continue
                if ((type!!.type == MapMarker.END || type.type == MapMarker.OPEN)) neighbors.add(neighbor)
            }
        }
        return neighbors
    }

    private fun getPathResults(start: Point, end: Point) {
        var currentPoint = start
        while (currentPoint != end) {
            visitedPoints.add(currentPoint)
            currentPoint = findValidNeighbor(currentPoint)
        }
        //Add End
        visitedPoints.add(currentPoint)
    }

    private fun findValidNeighbor(currentPoint: Point): Point {
        val directions = arrayOf(intArrayOf(-1, 0), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(0, 1))
        var next = Point(0, 0)
        for (direction in directions) {
            val neighbor = Point(currentPoint.x + direction[0], currentPoint.y + direction[1])
            //If it's not a wall and we haven't visited (prevent going backward)
            val type = raceMap[neighbor]
            if (raceMap[neighbor] == null) continue
            if ((type!!.type == MapMarker.END || type.type == MapMarker.OPEN) && !visitedPoints.contains(neighbor)) return neighbor
        }
        return next

    }

    fun printallPaths(path: List<Point>) {
        val maxX = raceMap.keys.maxOf { it.x }
        val maxY = raceMap.keys.maxOf { it.y }

        for (y in 0..maxY) {
            for (x in 0..maxX) {
                var char = '.'
                val point = Point(x, y)
                val racePoint = raceMap[point]!!.type
                if (racePoint == MapMarker.OPEN && path.contains(point)) {
                    char = 'h'
                } else {
                    char = when (racePoint) {
                        MapMarker.WALL -> '#'
                        MapMarker.START -> 'S'
                        MapMarker.END -> 'E'
                        MapMarker.OPEN -> '.'
                    }
                }
                print(char)
            }
            println()
        }
    }
}

//    fun findAllPathsWithCheats(
//        map: Map<Point, RacePoint>,
//        start: Point,
//        end: Point
//    ) {
//        //Pair<List<List<Point>>,Map<Point,Int>> {
//        //val allPaths = mutableListOf<List<Point>>()
//        val currentPath = mutableListOf<Point>()
//        val visited = mutableSetOf<Point>()
//        //val cheatTally = mutableMapOf<Point,Int>()
//        //val noneCheatPath = mutableListOf<List<Point>>()
//        val state = RaceState(start, start, end, null, null)
//
//        fun depthFirstSearch(state: RaceState) {
//            currentPath.add(state.currentPoint)
//            visited.add(state.currentPoint)
//
//            if (state.currentPoint == end) {
//                if (state.usedCheatOne != null) {
//                    cheatTally[state.usedCheatOne!!] = cheatTally.getOrPut(state.usedCheatOne!!, { 0 }) + 1
//                    //cheatPaths.add(Pair(state.usedCheatOne!!, currentPath.toList()))
//                } else {
//                    noneCheatPath.add(currentPath.toList())
//                }
//                //allPaths.add(currentPath.toList()) // Add a copy of the path
//            } else {
//                for (neighbor in getNeighbors(state, map)) {
//                    if (!visited.contains(neighbor.currentPoint)) {
//                        depthFirstSearch(neighbor)
//                    }
//                }
//            }
//
//            visited.remove(state.currentPoint)
//            currentPath.removeLast()
//        }
//        depthFirstSearch(state)
//        // return Pair(allPaths, cheatTally)
//    }
//
//    private fun getNeighbors(state: RaceState, map: Map<Point, RacePoint>): List<RaceState> {
//        val neighbors = mutableListOf<RaceState>()
//        val directions = arrayOf(intArrayOf(-1, 0), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(0, 1))
//
//        for (dir in directions) {
//            val neighbor = Point(state.currentPoint.x + dir[0], state.currentPoint.y + dir[1])
//            if (map.containsKey(neighbor) && map[neighbor]!!.type != MapMarker.WALL) {
//                val newState = RaceState(neighbor, state.start, state.end, state.usedCheatOne, state.usedCheatTwo)
//                //We can cheat two walls and that is it
////                if (map[neighbor]!!.type == MapMarker.WALL) {
////                    if (newState.usedCheatOne == null) {
////                        newState.usedCheatOne = neighbor
////                    }
//////                    else if(newState.usedCheatTwo == null){
//////                        newState.usedCheatTwo = neighbor
//////                    }
////                    //If we used all our cheats so far we can't continue
////                    else {
////                        continue
////                    }
////                }
//                neighbors.add(newState)
//            }
//        }
//        return neighbors
//    }

