package advent2024.Day8

import advent2024.util.FileReaderUtil
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sign
import kotlin.math.sqrt

class Day8Puzzle {

    data class SignalMapPoint(val xPosition: Int, val yPosition: Int) {

    }

    var nodes = mutableListOf<SignalMapPoint>()
    var mapOfSignals = listOf<Pair<SignalMapPoint, Char>>()
    var maxRow = 0
    var maxColumn = 0
    fun solve() {
        val inputLines = FileReaderUtil().readFileAsLines("Day8.txt")
        maxRow = inputLines!!.size
        maxColumn = inputLines[0].length
        mapOfSignals = inputLines!!.flatMapIndexed { yIndex, row ->
            row.mapIndexed { xIndex, char ->
                SignalMapPoint(
                    xIndex,
                    yIndex
                ) to char
            }
        }
        val typesOfSignals = mapOfSignals.map { it.second }.filterNot { it == '.' }.toSet()
        for (index in typesOfSignals.indices) {
            findAllNodes(mapOfSignals.filter { it.second == typesOfSignals.elementAt(index) })
        }
        println("Number of nodes: ${nodes.toSet().size}")
        nodes.clear()
        for (index in typesOfSignals.indices) {
            findAllNodesPart2(mapOfSignals.filter { it.second == typesOfSignals.elementAt(index) })
        }
        println("Number of nodes: ${nodes.toSet().size}")
    }

    fun findAllNodes(points: List<Pair<SignalMapPoint, Char>>) {
        for (i in points.indices) {
            for (j in i + 1 until points.size) {
                val firstPoint = points[i]
                val secondPoint = points[j]
                val nextPoints = getNextTwoPointsInLine(firstPoint.first, secondPoint.first)
                if (checkIfInSideMap(nextPoints.first)) nodes.add(nextPoints.first)
                if (checkIfInSideMap(nextPoints.second)) nodes.add(nextPoints.second)
            }
        }
    }

    fun findAllNodesPart2(points: List<Pair<SignalMapPoint, Char>>) {
        for (i in points.indices) {
            for (j in i + 1 until points.size) {
                var firstPoint = points[i].first
                var secondPoint = points[j].first
                var firstOnMap = true
                var secondOnMap = true
                val dx = firstPoint.xPosition - secondPoint.xPosition
                val dy = secondPoint.yPosition - firstPoint.yPosition
                while (firstOnMap || secondOnMap) {
                    //Loop until we go off the map on both points,
                    val nextPointOne = SignalMapPoint(firstPoint.xPosition + dx, firstPoint.yPosition - dy)
                    val nextPointTwo = SignalMapPoint(secondPoint.xPosition - dx, secondPoint.yPosition + dy)
                    firstOnMap = checkIfInSideMap(nextPointOne)
                    secondOnMap = checkIfInSideMap(nextPointTwo)
                    //If on the map add them to nodes
                    if (firstOnMap) nodes.add(nextPointOne)
                    if (secondOnMap) nodes.add(nextPointTwo)
                    firstPoint = nextPointOne
                    secondPoint = nextPointTwo
                }
            }
            //If there is at least 2 "frequencies" turn each point into a node
            if (points.size != 1) nodes.add(points[i].first)
        }
    }

    fun checkIfInSideMap(point: SignalMapPoint): Boolean {
        return point.xPosition > -1 && point.xPosition < maxColumn && point.yPosition > -1 && point.yPosition < maxRow
    }

    fun getNextTwoPointsInLine(
        firstPoint: SignalMapPoint,
        secondPoint: SignalMapPoint
    ): Pair<SignalMapPoint, SignalMapPoint> {
        val dx = firstPoint.xPosition - secondPoint.xPosition
        val dy = secondPoint.yPosition - firstPoint.yPosition
        val nextPointOne = SignalMapPoint(firstPoint.xPosition + dx, firstPoint.yPosition - dy)
        val nextPointTwo = SignalMapPoint(secondPoint.xPosition - dx, secondPoint.yPosition + dy)
        return Pair(nextPointOne, nextPointTwo)
    }


}