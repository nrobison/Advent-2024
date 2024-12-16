package advent2024.Day14

import advent2024.util.FileReaderUtil
import advent2024.util.Point


class Day14Puzzle {

    val maxX = 101 //Example is 11 puzzle is 101
    val maxY = 103 //Example is 7 puzzle is 103
    fun solve() {
        val fileReaderUtil = FileReaderUtil()
        val lines = fileReaderUtil.readFileAsLines("Day14.txt")
        val listOfRobots = mutableListOf<Pair<Point, Point>>()
        for (line in lines!!) {
            var robot = fileReaderUtil.readLineIntoTwoPairsOfInts(line)
            if (robot != null) {
               // robot = moveRobot(robot, 100)
                listOfRobots.add(robot)
            }
        }
        solvePart1(listOfRobots.toMutableList().map {moveRobot(it,100) }.toMutableList())
        solvePart2(listOfRobots.toList())

    }

    //Help from Reddit user chicagocode, I do not own this solution, I couldn't find a way to use Chinese Remainder Theorem
    //TODO: Revisit this and try again using Remainder Theorem
    fun solvePart2(robots: List<Pair<Point, Point>>){
        var moves = 0
        var robotsInTurn = robots.toList()
        while( robotsInTurn.distinctBy({ it.first}).size != robotsInTurn.size){
            moves++
            robotsInTurn = robotsInTurn.map { moveRobot(it,1)}.toMutableList()
        }
        println("Found the Christmas Tree, moves needed: $moves")
    }

    fun solvePart1(robots: MutableList<Pair<Point,Point>>) {
        var safetyFactor = 1
        //Break down to quadrants but exclude the center line (mid point)
        val quadrants = getQuadrants(false)
        for(quad in quadrants){
            val countOfQuad = robots.count { it.first.x >= quad.first.x && it.first.x < quad.second.x && it.first.y >= quad.first.y && it.first.y < quad.second.y }
            safetyFactor *= countOfQuad
            println("Quad contains : $countOfQuad robots in it")
        }
        println("Safety Factor: $safetyFactor")
    }
    fun getQuadrants(includeMiddles: Boolean): List<Pair<Point, Point>> {
        //Need to use 1..Max so -1 to these odd numbers Mid of 7 is actually 3 [0,1,2] 3 [4,5,6]
        val midX = (maxX-1) / 2
        val midY = (maxY-1) / 2
        if(!includeMiddles)
        return listOf(
            //Top Left
            Pair(Point(0,0), Point(midX,midY)),
            //Top Right
            Pair(Point(midX+1,0), Point(maxX,midY)),
            //Bottom Left
            Pair(Point(0, midY+1), Point(midX, maxY)),
            //Bottom Right
            Pair(Point(midX+1,midY+1),Point(maxX,maxY))
        )
        else
            return listOf(
                //Top Left
                Pair(Point(0,0), Point(midX,midY)),
                //Top Right
                Pair(Point(midX,0), Point(maxX,midY)),
                //Bottom Left
                Pair(Point(0, midY), Point(midX, maxY)),
                //Bottom Right
                Pair(Point(midX,midY),Point(maxX,maxY))
            )
    }

    fun moveRobot(robot: Pair<Point,Point>, numberOfMoves: Int) : Pair<Point,Point>{
        //Repeat the moves
        for(i in 0 until numberOfMoves){
            robot.first.plus(robot.second)
            //Check if we need to wrap around 101 - 101 = 0
            if(robot.first.x >= maxX) robot.first.x -= maxX
            //Wrap the other way -1 + 101 = 100
            if(robot.first.x < 0) robot.first.x += maxX
            //Y wrap
            if(robot.first.y >= maxY) robot.first.y -= maxY
            //Y warp other way
            if(robot.first.y < 0) robot.first.y += maxY
        }
        return Pair(Point(robot.first.x, robot.first.y),Point(robot.second.x,robot.second.y))
    }
}