package advent2024.Day6

import advent2024.util.FileReaderUtil

class Day6Puzzle {

    //Convenient way to keep directions and not rewrite them
    object Directions {
        val UP = MapPoint(0, -1)
        val RIGHT = MapPoint(1, 0)
        val DOWN = MapPoint(0, 1)
        val LEFT = MapPoint(-1, 0)
        val NONE = MapPoint(0,0)

        fun getDirectionChar(spot: Char) =
            when(spot) {
                '^' -> UP
                '>' -> RIGHT
                'v' -> DOWN
                '<' -> LEFT
                else -> NONE
            }
        fun rotate90Degrees(direction: MapPoint) =
            when(direction){
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
                else -> NONE
            }
    }
    //Class that holds our GuardPosition. Also handles changing direction
    data class MapPoint(val xPosition: Int, val yPosition: Int) {
        //Default is NONE don't assume direction
        var direction = Directions.NONE

        //Add points essentially moving the Guard from one point to another based on direction
         fun moveToNextPosition(movement: MapPoint) =
            MapPoint(xPosition + movement.xPosition, yPosition + movement.yPosition)

    }

    fun solve(){
        val readLines = FileReaderUtil().readFileAsLines("Day6.txt")
        //Convert this list of strings to a map which is a position (MapPosition)
        val map = readLines!!.flatMapIndexed{yIndex, row -> row.mapIndexed {xIndex, char -> MapPoint(xIndex,yIndex) to char}  }.toMap().withDefault{' '}
        val resultOfNavigation = navigateTheMap(map)
        println("Part 1 Total moves til off: ${resultOfNavigation.first.size}")
        //Starting point probably could be higher up? Should have considered as part of round 1
        val startingPoint = map.entries.first{that ->that.value != '.' && that.value != '#'}.key
        //Get list of all points except starting point. Navigate through placing the
        val part2 = resultOfNavigation.first.filterNot{it == startingPoint}.count{ placedItem -> navigateTheMap(map,placedItem).second}
        println("Part 2 Total Loops $part2")

    }

    fun navigateTheMap(map: Map<MapPoint, Char>,placedItem: MapPoint? = null): Pair<Set<MapPoint>, Boolean> {
        var position = map.entries.first{it.value != '.' && it.value != '#'}.key
        //Get position but don't assume its one way. Use the Directions Object to return it
        position.direction = Directions.getDirectionChar(map[position]!!)
        var directionToGo = position.direction
        //Keep a set of positions visited
        val visitedPositions = mutableSetOf<Pair<MapPoint,MapPoint>>()

        while(position in map && (position to directionToGo) !in visitedPositions){
            visitedPositions.add(position to directionToGo)
            //Move the direction needed
            val nextPosition = position.moveToNextPosition(directionToGo)
            if(map.getValue(nextPosition) == '#' || nextPosition == placedItem){
                directionToGo = Directions.rotate90Degrees(directionToGo)
            }
            else{
                position = nextPosition
            }
        }

        return Pair(
            //Get just the first of the pairs, first is the setting position second is the moved to
            visitedPositions.map { it.first }.toSet(),
            //Was it a loop
            position to directionToGo in visitedPositions
        )
    }

}
