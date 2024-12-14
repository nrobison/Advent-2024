package advent2024.Day13

import advent2024.util.FileReaderUtil

class Day13Puzzle {
    data class Point(val x: Long, val y: Long)

    fun solve(part2: Boolean) {
        var tokens = 0L
        val offset = if(part2) 10000000000000L else 0L
        val fileReader = FileReaderUtil()
        var lines = fileReader.readFileAsLines("Day13.txt")!!.filter { it.isNotEmpty() }
        for (i in 0 until lines.size step 3) {

            val buttonALine = parseLineForButtonOrPrize(lines[i])
            val buttonBLine = parseLineForButtonOrPrize(lines[i + 1])
            var prizeLine = parseLineForButtonOrPrize(lines[i + 2])
            //Got to add offset, easier to do after we parse it
            prizeLine = Point(prizeLine!!.x + offset,prizeLine.y + offset)
            val det = (buttonALine!!.x * buttonBLine!!.y  - buttonALine.y * buttonBLine.x)
            if(det == 0L) continue

            val a = (prizeLine.x * buttonBLine.y - prizeLine.y * buttonBLine.x ).toDouble() / det
            val b =  (buttonALine.x * prizeLine.y - buttonALine.y * prizeLine.x).toDouble() / det
            if (a.isFinite() && b.isFinite()) { // Check for NaN or Infinite
                val aInt = a.toLong()
                val bInt = b.toLong()

                if (buttonALine.x * aInt + buttonBLine.x * bInt == prizeLine.x && buttonALine.y * aInt + buttonBLine.y * bInt == prizeLine.y) {
                    tokens += (aInt * 3 + bInt)
                   // println("Found a solution for Prize: X=${prizeLine.x}, Y=${prizeLine.y}")
                } else {
                   // println("No match found ")
                }
            }
        }

        println("Tokens needed for all prizes: $tokens")

    }
    //Parse Button A: or Button B: or Prize Line
    fun parseLineForButtonOrPrize(line: String): Point? {
        val regex = Regex("X\\+?=?(-?\\d+), Y\\+?=?(-?\\d+)")
        val matchResult = regex.find(line)
        return matchResult?.let {
            val (x, y) = it.destructured
            Point(x.toLong(), y.toLong())
        }
    }

}