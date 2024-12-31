package advent2024.Day16

import advent2024.util.FileReaderUtil
import advent2024.util.MapMarker
import advent2024.util.RacePoint
import java.util.PriorityQueue

typealias Node = Pair<Int,Int>

class Day16Puzzle {
    enum class Direction {
        EAST, SOUTH, WEST, NORTH
    }

    var maxY = 0
    var maxX = 0
    fun solve() {
        val input = FileReaderUtil().readFileAsLines("Day16.txt")

        val raceMap = input!!.flatMapIndexed { yIndex, row ->
            if (yIndex > maxY) maxY = yIndex
            row.toCharArray().mapIndexed { xIndex, char ->
                if (xIndex > maxX) maxX = xIndex
                Node(xIndex, yIndex) to RacePoint(char)
            }
        }.toMap().withDefault { RacePoint(' ') }
        val start = raceMap.entries.find { it.value.type == MapMarker.START }!!.key
        val end = raceMap.entries.find { it.value.type == MapMarker.END }!!.key
        val returned = findPaths(raceMap,start,end)
        println("Total Cost: ${returned.first} and number of prime seats ${returned.second}")

    }

    fun findPaths(raceMap: Map<Node,RacePoint>, start: Node, end: Node): Pair<Int, Int> {
        val queue = PriorityQueue<Triple<List<Node>, Direction, Int>>(compareBy { it.third })
        var minCost = Int.MAX_VALUE
        val seats  = mutableSetOf<Node>()
        val visited = mutableMapOf<Pair<Node,Direction>,Int>()

        queue.add(Triple(listOf(start),Direction.EAST,0))

        while(queue.isNotEmpty()){
            val current = queue.poll()
            val node = current.first.last() to current.second
            val costKnown = visited.getOrDefault(node,Int.MAX_VALUE)
            if(current.third < costKnown) visited[node] = current.third

            if(current.first.last() == end){
                //Break out early if we find a longer path
                if(current.third > minCost){ return minCost to seats.size }
//                for(y in 0..maxY){
//                    var line = ""
//                    for(x in 0..maxX){
//                        if(current.first.contains(Node(x,y))) line+= "0"
//                        else line+= raceMap[Node(x,y)]!!.char
//                    }
//                    println(line)
//                }
                //Somehow my +1 is off a bit here. Doing some division and size
                minCost = ((current.third.floorDiv(1000)*1000)) + current.first.size-1
                seats += current.first
            }
            //if cost is less than best known keep going
            if(current.third <= costKnown){
                val directions = getValidDirectionNodes(raceMap,current)
                directions.forEach{
                    queue.add(it)
                }
            }
        }
        return minCost to seats.size
    }

    fun getValidDirectionNodes(raceMap: Map<Node,RacePoint>,path: Triple<List<Node>,Direction,Int>): List<Triple<List<Node>,Direction,Int>>{
        val rightDirection = getNewDirectionPoint(turnRight(path.second), path.first.last())
        val leftDirection = getNewDirectionPoint(turnLeft(path.second),path.first.last())
        val continueDirection = getNewDirectionPoint(path.second,path.first.last())
        val directionsToReturn = mutableListOf<Triple<List<Node>,Direction,Int>>()
        val oldPathToAdd = path.first.toMutableList()
        if(raceMap[rightDirection]!!.type != MapMarker.WALL) {
            val newRight = oldPathToAdd.toMutableList()
            newRight.add(rightDirection)
            directionsToReturn.add(
                Triple(
                   newRight,
                    turnRight(path.second),
                    path.third + 1000
                )
            )
        }
        if(raceMap[leftDirection]!!.type != MapMarker.WALL){
            val newLeft =  oldPathToAdd.toMutableList()
            newLeft.add(leftDirection)
            directionsToReturn.add(Triple(newLeft,turnLeft(path.second),path.third +1000))
        }
        if(raceMap[continueDirection]!!.type != MapMarker.WALL){
            val newContinue = oldPathToAdd.toMutableList()
            newContinue.add(continueDirection)
            directionsToReturn.add(Triple(newContinue,path.second,path.third + 1))
        }
        return directionsToReturn
    }

    fun getNewDirectionPoint(direction: Direction, node: Node) : Node{
        if(direction == Direction.EAST) return Node(node.first + 1, node.second)
        if(direction == Direction.WEST) return Node(node.first -1, node.second)
        if(direction == Direction.NORTH) return Node(node.first,node.second-1)
        if(direction == Direction.SOUTH) return Node(node.first,node.second+1)
        return Node(0,0)
    }
        fun turnRight(direction: Direction): Direction {
        return when (direction) {
            Direction.NORTH -> Direction.EAST
            Direction.EAST -> Direction.SOUTH
            Direction.SOUTH -> Direction.WEST
            Direction.WEST -> Direction.NORTH
            else -> throw IllegalArgumentException("Invalid direction")
        }
    }

    fun turnLeft(direction: Direction): Direction {
        return when (direction) {
           Direction.NORTH -> Direction.WEST
            Direction.WEST -> Direction.SOUTH
            Direction.SOUTH -> Direction.EAST
            Direction.EAST -> Direction.NORTH
            else -> throw IllegalArgumentException("Invalid direction")
        }
    }

}