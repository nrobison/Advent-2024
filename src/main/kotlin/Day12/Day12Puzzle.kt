package advent2024.Day12

import advent2024.util.FileReaderUtil

//Makes adding pairs cleaner
private fun Pair<Int, Int>.plus(up: Pair<Int, Int>): Pair<Int,Int> {
    return Pair(this.first + up.first, this.second + up.second)
}

class Day12Puzzle {
    var price = 0
    var visitedSpots = mutableMapOf<Pair<Int,Int>,Char>()
    var map = emptyArray<CharArray>()
    var setPlots = mutableListOf<MutableList<Pair<Int,Int>>>()
    var plotCount =0
    var cornerTotalPrice = 0

    fun solve(){

        val fileReader = FileReaderUtil()
        map = fileReader.convertLinesToArrayOfChars(fileReader.readFileAsLines("Day12.txt")!!)
        findPlot()
    }

    fun findPlot() {
        //Start at 0,0 with that Char, DFS finding the edges
        //Check Up,down,left,right if the next item is the same char, if so advance to it
        //if any side is outside of grid bounds or not the same char add an edge
        var count = 0
        var edges = 0
        fun traverse(xPosition: Int, yPosition: Int) {
            //If we already visited it don't do anything break
            if(visitedSpots.containsKey(Pair(xPosition,yPosition))) return
            count++
            //This needs to be added before jumping into directions
            visitedSpots[Pair(xPosition,yPosition)] = map[yPosition][xPosition]
            setPlots[plotCount].add(Pair(xPosition,yPosition))


            val directions = listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))
            for(direction in directions){
                //If we already visited we will continue
                if(visitedSpots.containsKey(Pair((xPosition + direction.first),(yPosition + direction.second)))) {
                    //If the visited spot is not the same Char we add an edge
                    if(map[(yPosition + direction.second)][xPosition + direction.first] != map[yPosition][xPosition]){
                        edges++
                    }
                    continue
                }

                if(nextMoveIsValid((xPosition + direction.first),(yPosition + direction.second), map[yPosition][xPosition])){
                    traverse(xPosition + direction.first,yPosition + direction.second)
                }
                else {
                    edges ++
                }
            }
        }

        for(i in  map.indices){
            for(j in map[0].indices){
                if(!visitedSpots.containsKey(Pair(j,i))) {
                    setPlots.add(mutableListOf<Pair<Int,Int>>())
                    traverse(j, i)
                    val cornerFind = findCorners(setPlots[plotCount])
//                    if (edges != 0) println(
//                        "Current position ($j,$i) char: ${map[i][j]} and count = $count" +
//                                " and edges = $edges and price = ${count * edges} AND sides = $cornerFind so bulk is ${cornerFind * count}"
//                    )
                    price += (edges * count)
                    cornerTotalPrice += (cornerFind * count)
                    edges = 0
                    count = 0
                    plotCount++
                }
            }
        }
        println("Total Bulk Price: $cornerTotalPrice")
        println("Total price: $price")

    }
    fun isValid(row: Int, col: Int): Boolean {
        return row in map.indices && col in map[0].indices
    }

    fun nextMoveIsValid(xPosition: Int, yPosition: Int, currentChar : Char) : Boolean{
        if(!isValid(xPosition,yPosition)) return false
        if(map[yPosition][xPosition] == currentChar) return true
        return false
    }

    fun findCorners(plotPoints :List<Pair<Int,Int>> ) : Int{
        var corners = 0
        val up = Pair(0,1)
        val right = Pair(1,0)
        val down = Pair(0,-1)
        val left = Pair(-1,0)

        for(point in plotPoints){
            val upAdded = point.plus(up)
            val rightAdded = point.plus(right)
            val downAdded = point.plus(down)
            val leftAdded = point.plus(left)
            //Up and left are different we have a corner
            if(!plotPoints.contains(upAdded) && !plotPoints.contains(leftAdded)) {
                corners++
            }
            //Up and Right are different (meaning they aren't in our region) it's a corner
            if(!plotPoints.contains(upAdded) && !plotPoints.contains(rightAdded)) {
                corners++
            }
            //Down and left are different it's a corner
            if(!plotPoints.contains(downAdded) && !plotPoints.contains(leftAdded)) {
                corners++
            }
            //Down and right are different it's a corner
            if(!plotPoints.contains(downAdded) && !plotPoints.contains(rightAdded)) {
                corners++
            }
            //IF Up and left are in the map but Up+Left isn't it's a corner
            if(plotPoints.contains(upAdded) && plotPoints.contains(leftAdded) && !plotPoints.contains(upAdded.plus(left))) {
                corners++
            }
            //If Up and right are in the map but Up + Right isn't it's a corner
            if(plotPoints.contains(upAdded)  && plotPoints.contains(rightAdded) && !plotPoints.contains(upAdded.plus(right))) {
                corners++
            }
            //If Down and Left are in the map but Down + Left isn't it's a corner
            if(plotPoints.contains(downAdded) && plotPoints.contains(leftAdded) &&!plotPoints.contains(downAdded.plus(left))) {
                corners++
            }
            //If Down and Right are in the map but Down + Right isn't it's a corner
            if(plotPoints.contains(downAdded) && plotPoints.contains(rightAdded)  &&!plotPoints.contains(downAdded.plus(right))) {
                corners++
            }
        }
        return corners
    }



}



