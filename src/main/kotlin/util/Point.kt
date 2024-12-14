package advent2024.util

import advent2024.Day13.Day13Puzzle

data class Point(var x: Int, var y: Int) {

     fun plus (pointToAdd : Point){
        x += pointToAdd.x
        y += pointToAdd.y
     }

    override fun equals(other: Any?): Boolean {
        if(other is Point){
            return ((other.x == x ) && (other.y == y))
        }
        return false
    }
}