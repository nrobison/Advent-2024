package advent2024.Day15

import advent2024.util.FileReaderUtil
import advent2024.util.Point

class Day15Puzzle {

    enum class MovementDirection{
        UP,
        RIGHT,
        DOWN,
        LEFT
    }
    data class Direction(val movement: Point, val movementDirection: MovementDirection ){

    }

    val walls = mutableListOf<Point>()
    val boxes = mutableListOf<Point>()
    val moves = mutableListOf<Direction>()
    var robot = Point(0,0)
    var boxesPartTwo = mutableListOf<Pair<Point,Point>>()
    var copyOfBoxesPartTwo = mutableListOf<Pair<Point,Point>>()
    val visitedBoxes = mutableListOf<Point>()
    fun solve(){
        val fileReader = FileReaderUtil()
        val linesOfText = fileReader.readFileAsLines("Day15.txt")
        //Probably could use a map and a function here
        generatePartOneMap(linesOfText)
        //printMap()
        moves.forEach {
            val nextSpot = Point(robot.x,robot.y)
            nextSpot.plus(it.movement)
            val shouldMoveRobot = applyMovement(nextSpot,it)
            if(shouldMoveRobot) robot.plus(it.movement)
          //  printMap()
        }
        //printMap()
        calcuateGPSSum()
        walls.clear()
        boxes.clear()
        moves.clear()
        generatePartTwoMap(linesOfText)
        //printMapPartTwo()
        moves.forEach {
            val nextSpot = Point(robot.x,robot.y)
            nextSpot.plus(it.movement)

            var shouldMoveRobot = false
            copyOfBoxesPartTwo = boxesPartTwo.toMutableList()
            if(it.movementDirection == MovementDirection.LEFT || it.movementDirection == MovementDirection.RIGHT){
                shouldMoveRobot = applyMovePartTwoHorizontal(nextSpot,it)
            }
            else{
                shouldMoveRobot = applyMovePartTwoVertical(nextSpot,it)
            }
            if(shouldMoveRobot) robot.plus(it.movement)
            //If not successful we revert the state
            else boxesPartTwo = copyOfBoxesPartTwo.toMutableList()
            visitedBoxes.clear()
           // println("Movement: ${printMovementChar(it.movementDirection)}")
           // printMapPartTwo()
        }
        calculateGPSSumPartTwo()
    }

    fun generatePartOneMap(linesOfText: List<String>?){
        for(line in linesOfText!!.indices){
            if(linesOfText[line].isEmpty()) continue
            if(linesOfText[line][0] == '#'){
                for(i in 0 until linesOfText[line].lastIndex +1){
                    val char = linesOfText[line][i]
                    val point = Point(i,line)
                    when (char) {
                        '@' -> robot = point
                        '#' -> walls.add(point)
                        'O' -> boxes.add(point)
                    }
                }
            }
            //We are in instructions
            else{
                linesOfText[line].forEach { addMovement(it) }
            }
        }
    }
    fun generatePartTwoMap(linesOfText: List<String>?){
        var x = 0
        for(line in linesOfText!!.indices){
            if(linesOfText[line].isEmpty()) continue
            if(linesOfText[line][0] == '#'){
                x = 0
                for(i in 0 until linesOfText[line].lastIndex +1){
                    val char = linesOfText[line][i]
                    val point = Point(x,line)
                    when (char) {
                        '@' -> {
                            robot = point
                            x++
                        }
                        '#' -> {
                            walls.add(point)
                            x++
                            walls.add(Point(x,line))
                        }
                        'O' -> {
                            x++
                            val addedBoxPoint = Point(x,line)
                            //point.linkedPoint = addedBoxPoint
                            //boxes.add(point)
                            //boxes.add(addedBoxPoint)
                            boxesPartTwo.add(Pair(addedBoxPoint,point))
                        }
                        else -> x++
                    }
                    x++
                }
            }
            //We are in instructions
            else{
                linesOfText[line].forEach { addMovement(it) }
            }
        }
    }

    fun calcuateGPSSum(){
        var total = 0
        boxes.forEach {
            total+= (100 * it.y + it.x )
        }
        println("Total of GPS Part One is: $total")
    }

    fun calculateGPSSumPartTwo(){
        var total = 0
        boxesPartTwo.forEach {
            var pointToUse = if(it.first.x + it.first.y < it.second.x + it.second.y) it.first else it.second
            total += (100 * pointToUse.y + pointToUse.x)
        }
        println("Total of GPS Part Two is: $total")

    }

    fun applyMovePartTwoHorizontal(movingPoint: Point,movement: Direction) : Boolean{
        if(walls.contains(movingPoint)) return false
        val copyOfPoint = movingPoint.clone()
        //Apply the movement one more time to see connected spot
        copyOfPoint.plus(movement.movement)
        //if(boxesPartTwo.contains(Pair(movingPoint,copyOfPoint))){
          if(containsPair(movingPoint,copyOfPoint)){
            //Check if nextPoint is connected Box if so we swap to that as the plus point
            //Double the movement because the next spot is the connected box
            val copyMoving = movingPoint.clone()
            movingPoint.plus(movement.movement)
            movingPoint.plus(movement.movement)
            val shouldMove = applyMovePartTwoHorizontal(movingPoint.clone(),movement)
            //If it's a valid move delete current box and add new
            if(shouldMove){
                //If we can move we take out the old part and advance it. [ ]
                boxesPartTwo.remove(Pair(copyMoving,copyOfPoint))
                boxesPartTwo.remove(Pair(copyOfPoint,copyMoving))
                boxesPartTwo.add(Pair(movingPoint,copyOfPoint))
            }
            else
                return false
        }
        //If the next point (moving point) is not in boxes or wall we can move (it's an empty space)
        return true
    }
    //If we provide no connected point default to moving point
    fun applyMovePartTwoVertical(movingPoint: Point, movement: Direction): Boolean {
        if(walls.contains(movingPoint)) return false
        //Add this current point to visited so we don't revisit connected Points
        visitedBoxes.add(movingPoint)
        //Got left and right points
        val copyOfPointLeft = movingPoint.clone()
        copyOfPointLeft.plus(Point(-1,0))
        val copyOfPointRight = movingPoint.clone()
        copyOfPointRight.plus(Point(1,0))
        var useLeft = false

        //If there is no linked point it's clear
        var linkedPoint = boxesPartTwo.firstOrNull{ it == Pair(movingPoint,copyOfPointLeft) }?: boxesPartTwo.firstOrNull{it == Pair(movingPoint,copyOfPointRight)}
        if(linkedPoint == null) {
            linkedPoint = boxesPartTwo.firstOrNull{ it == Pair(copyOfPointLeft,movingPoint) }?: boxesPartTwo.firstOrNull{it == Pair(copyOfPointRight,movingPoint)}
        }
        if(linkedPoint == null) return true

        useLeft = linkedPoint.first == copyOfPointLeft || linkedPoint.second == copyOfPointLeft

        //Wall we can't move it if either of the points would be a wall
        //|| walls.contains(connectedPoint)) return false
        //Consider both sides of the box. Should probably use a key value pair here for quicker lookup
        // We probably should consider a list of visited points here
        if(containsPair(movingPoint,if(useLeft)copyOfPointLeft else copyOfPointRight)){ //|| boxes.contains(connectedPoint)){
            //Grab the connected point then check if it's been visited already if so don't recheck it
            val oldPoint = movingPoint.clone()
            val connectedPoint = if(useLeft)copyOfPointLeft.clone() else copyOfPointRight.clone()
            movingPoint.plus(movement.movement)
            //Check the first point for valid to move
            var shouldMove = applyMovePartTwoVertical(movingPoint.clone(),movement)
            var shouldMoveConnect = true
            if(!visitedBoxes.contains(connectedPoint)){
                connectedPoint.plus(movement.movement)
                shouldMoveConnect = applyMovePartTwoVertical(connectedPoint.clone(),movement)
            }
            if(shouldMoveConnect && shouldMove){
                boxesPartTwo.remove(Pair(oldPoint,if(useLeft)copyOfPointLeft else copyOfPointRight))
                //Cover both orders of it
                boxesPartTwo.remove(Pair(if(useLeft)copyOfPointLeft else copyOfPointRight,oldPoint))
                boxesPartTwo.add(Pair(movingPoint,connectedPoint))
            }
            //Shouldn't move so return false
            else{
                return false
            }
        }
        //Each "box" now has two points. We gotta get the linked point too
        return true
    }

    fun containsPair(pointOne: Point, pointTwo: Point): Boolean{
        return boxesPartTwo.any { (it.first == pointOne || it.first == pointTwo) && (it.second == pointOne || it.second == pointTwo)  }
    }


    fun applyMovement(movingPoint: Point, movement: Direction): Boolean {
        //We are passed the "Next movement"
        if(walls.contains(movingPoint)) return false
        if(boxes.contains(movingPoint)){
            val copyOfPoint = Point(movingPoint.x,movingPoint.y)
            movingPoint.plus(movement.movement)
            val shouldMove = applyMovement(Point(movingPoint.x,movingPoint.y),movement)
            //If it's a valid move delete current box and add new
            if(shouldMove){
                boxes.remove(copyOfPoint)
                boxes.add(movingPoint)
            }
            else
                return false
        }
        //If the next point (moving point) is not in boxes or wall we can move (it's an empty space)
        return true
    }

    fun printMap(){
        for(y in 0 until walls[walls.lastIndex].y+1){
            var line = ""
            var rightBox = false
            for(x in 0 until walls[walls.lastIndex].x+1){
                if(walls.contains(Point(x,y))) line+= "#"
                else if(boxes.contains(Point(x,y))) {
                    if(rightBox) line+= "]"
                    else line+= "["
                    rightBox = !rightBox
                }
                else if(robot != Point(x,y))line+= "."
                else line+= "@"
            }
            println(line)
        }
    }
    fun printMapPartTwo(){
        val (copyOfBoxesFirst,copyOfBoxesSecond) = boxesPartTwo.unzip()
        for(y in 0 until walls[walls.lastIndex].y+1){
            var line = ""
            var rightBox = false
            for(x in 0 until walls[walls.lastIndex].x+1){
                if(walls.contains(Point(x,y))) line+= "#"
                else if (copyOfBoxesFirst.contains(Point(x,y)) || copyOfBoxesSecond.contains(Point(x,y))){
                    if(rightBox) line+= "]"
                    else line+= "["
                    rightBox = !rightBox
                }
                else if(robot != Point(x,y))line+= "."
                else line+= "@"
            }
            println(line)
        }
    }



    fun addMovement(char: Char){
        if(char == '<') moves.add(Direction(Point(-1,0),MovementDirection.LEFT))
        if(char ==  '^') moves.add(Direction(Point(0,-1),MovementDirection.UP))
        if(char == '>') moves.add(Direction(Point(1,0),MovementDirection.RIGHT))
        if(char == 'v') moves.add(Direction(Point(0,1),MovementDirection.DOWN))
    }

    fun printMovementChar(move: MovementDirection): String{
        if(move == MovementDirection.UP) return "Up ^"
        if(move == MovementDirection.DOWN) return "Down v"
        if(move == MovementDirection.RIGHT) return "Right >"
        if(move == MovementDirection.LEFT) return "Left <"
        return "Error selecting movement?"
    }

}